package com.example.clonedoudesu

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.clonedoudesu.adapter.KomikAdapter
import com.example.clonedoudesu.viewmodel.KomikViewModel
import kotlinx.coroutines.Job // Import Job
import kotlinx.coroutines.delay // Import delay
import kotlinx.coroutines.isActive // Import isActive
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val komikViewModel: KomikViewModel by viewModels()
    
    private lateinit var rekomendasiAdapter: KomikAdapter
    private lateinit var terbaru2Adapter: KomikAdapter
    private lateinit var pustakaAdapter: KomikAdapter
    private lateinit var berwarnaAdapter: KomikAdapter
    private lateinit var komikPopulerAdapter: KomikAdapter

    private lateinit var searchView: SearchView
    private lateinit var searchResultsRecyclerView: RecyclerView
    private lateinit var searchResultsAdapter: KomikAdapter
    private lateinit var mainContentLayout: View 

    private var searchDebounceJob: Job? = null // Job untuk debouncing
    private val SEARCH_DEBOUNCE_DELAY_MS = 500L // Waktu tunda 500ms

    private val TAG_MAIN_ACTIVITY = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        rekomendasiAdapter = KomikAdapter()
        terbaru2Adapter = KomikAdapter()
        pustakaAdapter = KomikAdapter()
        berwarnaAdapter = KomikAdapter()
        komikPopulerAdapter = KomikAdapter()

        searchView = findViewById(R.id.search_view) 
        searchResultsRecyclerView = findViewById(R.id.search_results_recycler_view)
        searchResultsAdapter = KomikAdapter()
        mainContentLayout = findViewById(R.id.main_content_layout) 

        setupRecyclerView(R.id.rekomendasi_recycler_view, rekomendasiAdapter, "Rekomendasi", LinearLayoutManager.HORIZONTAL)
        setupRecyclerView(R.id.terbaru2_recycler_view, terbaru2Adapter, "TerbaruV2", LinearLayoutManager.HORIZONTAL)
        setupRecyclerView(R.id.pustaka_recycler_view, pustakaAdapter, "Pustaka", LinearLayoutManager.HORIZONTAL)
        setupRecyclerView(R.id.berwarna_recycler_view, berwarnaAdapter, "Berwarna", LinearLayoutManager.HORIZONTAL)
        setupRecyclerView(R.id.komik_populer_recycler_view, komikPopulerAdapter, "Komik Populer", LinearLayoutManager.HORIZONTAL)
        setupRecyclerView(searchResultsRecyclerView, searchResultsAdapter, "Search Results", LinearLayoutManager.VERTICAL)

        fetchAllData()
        observeData()
        observeSearchResults()
        setupSearchView()
    }

    private fun fetchAllData() {
        Log.d(TAG_MAIN_ACTIVITY, "Requesting home data fetches from ViewModel")
        komikViewModel.fetchRekomendasi()
        komikViewModel.fetchTerbaru2()
        komikViewModel.fetchPustaka()
        komikViewModel.fetchBerwarna()
        komikViewModel.fetchKomikPopuler()
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, adapter: KomikAdapter, sectionName: String, orientation: Int) {
        recyclerView.layoutManager = LinearLayoutManager(this, orientation, false)
        if (orientation == LinearLayoutManager.HORIZONTAL) {
            PagerSnapHelper().attachToRecyclerView(recyclerView)
        }
        recyclerView.adapter = adapter
        Log.d(TAG_MAIN_ACTIVITY, "Setup RecyclerView for $sectionName completed.")
    }

    private fun setupRecyclerView(recyclerViewId: Int, adapter: KomikAdapter, sectionName: String, orientation: Int) {
        val recyclerView = findViewById<RecyclerView>(recyclerViewId)
        if (recyclerView == null) {
            Log.e(TAG_MAIN_ACTIVITY, "RecyclerView not found for section: $sectionName with ID: $recyclerViewId.")
            return
        }
        setupRecyclerView(recyclerView, adapter, sectionName, orientation)
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchDebounceJob?.cancel() // Batalkan job debounce jika ada
                val submittedQuery = query?.trim()
                if (!submittedQuery.isNullOrBlank()) {
                    Log.d(TAG_MAIN_ACTIVITY, "Search submitted: $submittedQuery")
                    komikViewModel.searchKomik(submittedQuery) 
                }
                searchView.clearFocus() // Selalu hapus fokus setelah submit
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchDebounceJob?.cancel() // Batalkan job debounce sebelumnya
                val currentQuery = newText?.trim()

                if (!currentQuery.isNullOrBlank()) {
                    searchDebounceJob = lifecycleScope.launch {
                        delay(SEARCH_DEBOUNCE_DELAY_MS)
                        // Pastikan coroutine masih aktif dan query di SearchView masih sama
                        if (isActive && currentQuery == searchView.query.toString().trim()) {
                            Log.d(TAG_MAIN_ACTIVITY, "Debounced search for: $currentQuery")
                            komikViewModel.searchKomik(currentQuery)
                        }
                    }
                } else {
                    // Jika query kosong, langsung kosongkan hasil
                    Log.d(TAG_MAIN_ACTIVITY, "Search query is blank, clearing results.")
                    komikViewModel.searchKomik("")
                }
                return true
            }
        })

        val closeButton: View? = searchView.findViewById(androidx.appcompat.R.id.search_close_btn)
        closeButton?.setOnClickListener {
            searchDebounceJob?.cancel() // Batalkan job debounce jika ada
            searchView.setQuery("", false) 
            searchView.clearFocus()
            komikViewModel.searchKomik("") 
            showMainContent() 
        }
    }

    private fun observeData() {
        lifecycleScope.launch { repeatOnLifecycle(Lifecycle.State.STARTED) { komikViewModel.rekomendasi.collect { rekomendasiAdapter.submitList(it) } } }
        lifecycleScope.launch { repeatOnLifecycle(Lifecycle.State.STARTED) { komikViewModel.terbaru2.collect { terbaru2Adapter.submitList(it) } } }
        lifecycleScope.launch { repeatOnLifecycle(Lifecycle.State.STARTED) { komikViewModel.pustaka.collect { pustakaAdapter.submitList(it) } } }
        lifecycleScope.launch { repeatOnLifecycle(Lifecycle.State.STARTED) { komikViewModel.berwarna.collect { berwarnaAdapter.submitList(it) } } }
        lifecycleScope.launch { repeatOnLifecycle(Lifecycle.State.STARTED) { komikViewModel.komikPopuler.collect { komikPopulerAdapter.submitList(it) } } }
    }

    private fun observeSearchResults() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                komikViewModel.searchResults.collect { results ->
                    Log.d(TAG_MAIN_ACTIVITY, "Search results updated: ${results.size} items")
                    searchResultsAdapter.submitList(results)
                    if (results.isNotEmpty()) {
                        showSearchResults()
                    } else {
                        // Jika hasil kosong TAPI query di searchView TIDAK kosong,
                        // itu berarti pencarian tidak menghasilkan apa-apa (tetap tampilkan area search results).
                        // Jika query juga kosong (misal setelah tekan X atau dikosongkan manual), tampilkan konten utama.
                        if (searchView.query.isNullOrBlank()) {
                            showMainContent()
                        } else {
                            showSearchResults() 
                        }
                    }
                }
            }
        }
    }

    private fun showSearchResults() {
        Log.d(TAG_MAIN_ACTIVITY, "Showing search results, hiding main content.")
        searchResultsRecyclerView.visibility = View.VISIBLE
        mainContentLayout.visibility = View.GONE 
    }

    private fun showMainContent() {
        Log.d(TAG_MAIN_ACTIVITY, "Showing main content, hiding search results.")
        searchResultsRecyclerView.visibility = View.GONE
        mainContentLayout.visibility = View.VISIBLE 
        if (searchView.query.isNullOrBlank()) {
            // Tidak perlu clearFocus() di sini karena bisa mengganggu jika pengguna ingin mengklik konten utama
            // searchView.clearFocus()
        }
    }
}
