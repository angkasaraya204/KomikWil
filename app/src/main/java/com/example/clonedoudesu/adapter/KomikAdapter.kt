package com.example.clonedoudesu.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.clonedoudesu.DetailActivity
import com.example.clonedoudesu.R
import com.example.clonedoudesu.model.Komik

class KomikAdapter : ListAdapter<Komik, KomikAdapter.KomikViewHolder>(KomikDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KomikViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_komik, parent, false)
        return KomikViewHolder(view)
    }

    override fun onBindViewHolder(holder: KomikViewHolder, position: Int) {
        val komik = getItem(position)
        holder.bind(komik)
    }

    class KomikViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.item_komik_image)
        private val titleView: TextView = itemView.findViewById(R.id.item_komik_title)

        fun bind(komik: Komik) {
            // Set judul
            titleView.text = komik.judul

            // Load thumbnail pakai Glide
            Glide.with(itemView.context)
                .load(komik.thumbnail)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(imageView)

            // Klik item → buka DetailActivity dengan slug
            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, DetailActivity::class.java).apply {
                    // Membersihkan slug sebelum mengirimkannya
                    val rawSlug = komik.slug ?: "" // Ambil slug, pastikan tidak null
                    val cleanSlug = rawSlug.substringAfterLast('/') // Ambil bagian setelah '/' terakhir
                    putExtra("slug", cleanSlug)
                }
                context.startActivity(intent)
            }
        }
    }
}

class KomikDiffCallback : DiffUtil.ItemCallback<Komik>() {
    override fun areItemsTheSame(oldItem: Komik, newItem: Komik): Boolean {
        // Asumsi 'slug' adalah ID unik yang bersih setelah pemrosesan jika diperlukan
        // Atau jika 'Komik' memiliki ID unik lain, gunakan itu.
        return oldItem.slug == newItem.slug // Perlu dipastikan slug di sini adalah identifier yang konsisten
    }

    override fun areContentsTheSame(oldItem: Komik, newItem: Komik): Boolean {
        return oldItem == newItem
    }
}
