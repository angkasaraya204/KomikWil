package com.example.clonedoudesu.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clonedoudesu.model.Komik // Model internal aplikasi kita
import com.example.clonedoudesu.model.KomikDetail
import com.example.clonedoudesu.model.GenreDetail
import com.example.clonedoudesu.model.Chapter
import com.example.clonedoudesu.model.PustakaApiResponse
import com.example.clonedoudesu.model.BerwarnaApiResponse
import com.example.clonedoudesu.model.KomikPopulerApiResponse
import com.example.clonedoudesu.model.PopulerKomikItem
import com.example.clonedoudesu.model.KomikItemFromApi
import com.example.clonedoudesu.model.SearchApiResponse // Import data class SearchApiResponse
import com.example.clonedoudesu.model.SearchResultItem // Import data class SearchResultItem
import com.example.clonedoudesu.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class KomikViewModel : ViewModel() {
    private val TAG = "KomikViewModel" // Tag untuk logging

    private val _rekomendasi = MutableStateFlow<List<Komik>>(emptyList())
    val rekomendasi: StateFlow<List<Komik>> = _rekomendasi

    private val _terbaru2 = MutableStateFlow<List<Komik>>(emptyList())
    val terbaru2: StateFlow<List<Komik>> = _terbaru2

    private val _pustaka = MutableStateFlow<List<Komik>>(emptyList())
    val pustaka: StateFlow<List<Komik>> = _pustaka

    private val _berwarna = MutableStateFlow<List<Komik>>(emptyList())
    val berwarna: StateFlow<List<Komik>> = _berwarna

    private val _komikPopuler = MutableStateFlow<List<Komik>>(emptyList())
    val komikPopuler: StateFlow<List<Komik>> = _komikPopuler

    private val _detailKomik = MutableStateFlow<KomikDetail?>(null)
    val detailKomik: StateFlow<KomikDetail?> = _detailKomik

    private val _bacaChapter = MutableStateFlow<Chapter?>(null)
    val bacaChapter: StateFlow<Chapter?> = _bacaChapter

    private val _searchResults = MutableStateFlow<List<Komik>>(emptyList())
    val searchResults: StateFlow<List<Komik>> = _searchResults

    private val _genreDetail = MutableStateFlow<GenreDetail?>(null)
    val genreDetail: StateFlow<GenreDetail?> = _genreDetail

    // Fungsi mapping untuk KomikItemFromApi (Pustaka, Berwarna)
    private fun mapKomikItemFromApiToKomik(apiItem: KomikItemFromApi): Komik {
        val cleanSlug = apiItem.detailUrl?.substringAfterLast('/') ?: ""
        return Komik(
            judul = apiItem.title ?: "N/A",
            thumbnail = apiItem.thumbnail ?: "",
            slug = cleanSlug
        )
    }

    // Fungsi mapping untuk PopulerKomikItem (Komik Populer)
    private fun mapPopulerKomikItemToKomik(populerItem: PopulerKomikItem): Komik {
        val cleanSlug = populerItem.apiDetailLink?.substringAfterLast('/') ?: ""
        return Komik(
            judul = populerItem.title ?: "N/A",
            thumbnail = populerItem.thumbnail ?: "",
            slug = cleanSlug
        )
    }

    // Fungsi mapping baru untuk SearchResultItem (Hasil Pencarian)
    private fun mapSearchResultItemToKomik(searchItem: SearchResultItem): Komik {
        // Prioritaskan slug dari field "apiSlug" jika ada dan tidak kosong,
        // Jika tidak, coba ekstrak dari "href"
        val slugFromApiSlug = searchItem.apiSlug?.takeIf { it.isNotBlank() }
        val slugFromHref = searchItem.href?.trimEnd('/')?.substringAfterLast('/')
        val cleanSlug = slugFromApiSlug ?: slugFromHref ?: ""
        
        return Komik(
            judul = searchItem.title ?: "N/A",
            thumbnail = searchItem.thumbnail ?: "",
            slug = cleanSlug
            // Anda bisa menambahkan field lain dari SearchResultItem jika Komik Anda punya field tersebut
            // misal: type = searchItem.type, genre = searchItem.genre
        )
    }

    fun fetchRekomendasi() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.apiService.getRekomendasi()
                Log.d(TAG, "fetchRekomendasi success: ${response.size} items")
                _rekomendasi.value = response
            } catch (e: Exception) {
                Log.e(TAG, "Error fetchRekomendasi", e)
            }
        }
    }

    fun fetchTerbaru2() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.apiService.getTerbaru2()
                Log.d(TAG, "fetchTerbaru2 success: ${response.size} items")
                _terbaru2.value = response
            } catch (e: Exception) {
                Log.e(TAG, "Error fetchTerbaru2", e)
            }
        }
    }

    fun fetchPustaka() {
        viewModelScope.launch {
            try {
                val apiResponse = RetrofitInstance.apiService.getPustaka()
                val mappedList = apiResponse.results.map { mapKomikItemFromApiToKomik(it) }
                Log.d(TAG, "fetchPustaka success: ${mappedList.size} items mapped. Original page: ${apiResponse.page}")
                _pustaka.value = mappedList
            } catch (e: Exception) {
                Log.e(TAG, "Error fetchPustaka", e)
                 _pustaka.value = emptyList()
            }
        }
    }

    fun fetchBerwarna() {
        viewModelScope.launch {
            try {
                val apiResponse = RetrofitInstance.apiService.getBerwarna()
                if (apiResponse.status == true && apiResponse.data?.results?.isNotEmpty() == true) {
                    val mappedList = apiResponse.data.results.map { mapKomikItemFromApiToKomik(it) }
                    Log.d(TAG, "fetchBerwarna success: ${mappedList.size} items mapped. Original page: ${apiResponse.data.page}, Status: ${apiResponse.status}")
                    _berwarna.value = mappedList
                } else {
                    Log.w(TAG, "fetchBerwarna returned status false or empty results. Message: ${apiResponse.message}, Status: ${apiResponse.status}")
                    _berwarna.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetchBerwarna", e)
                _berwarna.value = emptyList()
            }
        }
    }

    fun fetchKomikPopuler() {
        viewModelScope.launch {
            try {
                val apiResponse = RetrofitInstance.apiService.getKomikPopuler()
                val allItems = mutableListOf<PopulerKomikItem>()
                apiResponse.manga?.items?.let { allItems.addAll(it) }
                apiResponse.manhwa?.items?.let { allItems.addAll(it) }
                apiResponse.manhua?.items?.let { allItems.addAll(it) }

                if (allItems.isNotEmpty()) {
                    val mappedList = allItems.map { mapPopulerKomikItemToKomik(it) }
                    Log.d(TAG, "fetchKomikPopuler success: ${mappedList.size} items mapped from manga, manhwa, manhua.")
                    _komikPopuler.value = mappedList
                } else {
                    Log.w(TAG, "fetchKomikPopuler returned no items from manga, manhwa, or manhua.")
                    _komikPopuler.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetchKomikPopuler", e)
                _komikPopuler.value = emptyList()
            }
        }
    }

    fun fetchDetailKomik(slug: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.apiService.getDetailKomik(slug)
                Log.d(TAG, "fetchDetailKomik success: slug=$slug → $response")
                _detailKomik.value = response
            } catch (e: Exception) {
                Log.e(TAG, "Error fetchDetailKomik slug=$slug", e)
            }
        }
    }

    fun fetchBacaChapter(slug: String, chapter: String) {
        viewModelScope.launch {
            try {
                _bacaChapter.value = RetrofitInstance.apiService.getBacaChapter(slug, chapter)
                Log.d(TAG, "fetchBacaChapter success")
            } catch (e: Exception) {
                Log.e(TAG, "Error fetchBacaChapter slug=$slug, chapter=$chapter", e)
            }
        }
    }

    fun searchKomik(keyword: String) {
        viewModelScope.launch {
            if (keyword.isBlank()) {
                _searchResults.value = emptyList() // Kosongkan hasil jika keyword kosong
                Log.d(TAG, "Search keyword is blank, clearing search results.")
                return@launch
            }
            try {
                val apiResponse = RetrofitInstance.apiService.searchKomik(keyword)
                if (apiResponse.status == true && apiResponse.data != null) {
                    val mappedList = apiResponse.data.map { mapSearchResultItemToKomik(it) }
                    Log.d(TAG, "searchKomik success: ${mappedList.size} items for keyword '${apiResponse.keyword}'. Total from API: ${apiResponse.total}")
                    _searchResults.value = mappedList
                } else {
                    Log.w(TAG, "searchKomik returned status false or no data. Message: ${apiResponse.message}, Keyword: ${apiResponse.keyword}")
                    _searchResults.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error searchKomik keyword=$keyword", e)
                _searchResults.value = emptyList()
            }
        }
    }

    fun fetchGenreDetail(slug: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.apiService.getGenreDetail(slug)
                Log.d(TAG, "fetchGenreDetail success: slug=$slug → $response")
                _genreDetail.value = response
            } catch (e: Exception) {
                Log.e(TAG, "Error fetchGenreDetail slug=$slug", e)
            }
        }
    }
}
