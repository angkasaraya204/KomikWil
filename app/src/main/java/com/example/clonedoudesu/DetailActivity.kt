package com.example.clonedoudesu

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.clonedoudesu.adapter.ChapterAdapter
import com.example.clonedoudesu.adapter.SimilarKomikAdapter
import com.example.clonedoudesu.model.KomikDetail
import com.example.clonedoudesu.viewmodel.KomikViewModel
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {
    private val komikViewModel: KomikViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val slug = intent.getStringExtra("slug") ?: ""

        val image = findViewById<ImageView>(R.id.detailThumbnail)
        val title = findViewById<TextView>(R.id.detailTitle)
        val description = findViewById<TextView>(R.id.detailDescription)
        val chapterRecycler = findViewById<RecyclerView>(R.id.chapterRecyclerView)
        val similarRecycler = findViewById<RecyclerView>(R.id.similarRecyclerView)

        // Setup RecyclerView
        chapterRecycler.layoutManager = LinearLayoutManager(this)
        similarRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Observe data dari ViewModel
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                komikViewModel.detailKomik.collect { detail: KomikDetail? ->
                    detail?.let {
                        title.text = it.title
                        description.text = it.description

                        Glide.with(this@DetailActivity)
                            .load(it.thumbnail)
                            .placeholder(R.drawable.placeholder_image)
                            .error(R.drawable.error_image)
                            .into(image)

                        // Pasang adapter chapters
                        chapterRecycler.adapter = ChapterAdapter(it.chapters)

                        // Pasang adapter similar komik
                        similarRecycler.adapter =
                            SimilarKomikAdapter(it.similarKomik) { similar ->
                                val newIntent =
                                    Intent(this@DetailActivity, DetailActivity::class.java)
                                
                                // Membersihkan slug sebelum mengirimkannya
                                val rawSlugFromServer = similar.slug
                                val cleanSlug = rawSlugFromServer.substringAfterLast('/')
                                
                                newIntent.putExtra("slug", cleanSlug)
                                startActivity(newIntent)
                            }
                    }
                }
            }
        }

        // Trigger fetch detail komik
        if (slug.isNotEmpty()) { // Pastikan slug tidak kosong sebelum fetch
            komikViewModel.fetchDetailKomik(slug)
        }
    }
}
