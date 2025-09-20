package com.example.clonedoudesu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.clonedoudesu.R
import com.example.clonedoudesu.model.SimilarKomik

class SimilarKomikAdapter(
    private val list: List<SimilarKomik>,
    private val onClick: (SimilarKomik) -> Unit
) : RecyclerView.Adapter<SimilarKomikAdapter.SimilarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_similar, parent, false)
        return SimilarViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: SimilarViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    class SimilarViewHolder(itemView: View, val onClick: (SimilarKomik) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.similarImage)
        private val title: TextView = itemView.findViewById(R.id.similarTitle)

        fun bind(komik: SimilarKomik) {
            title.text = komik.title
            Glide.with(itemView.context).load(komik.thumbnail).into(image)
            itemView.setOnClickListener { onClick(komik) }
        }
    }
}
