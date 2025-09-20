package com.example.clonedoudesu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clonedoudesu.R
import com.example.clonedoudesu.model.ChapterDetail

class ChapterAdapter(
    private val chapters: List<ChapterDetail>
) : RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chapter, parent, false)
        return ChapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChapterViewHolder, position: Int) {
        val chapter = chapters[position]
        holder.bind(chapter)
    }

    override fun getItemCount() = chapters.size

    class ChapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.chapterTitle)
        private val date: TextView = itemView.findViewById(R.id.chapterDate)

        fun bind(chapter: ChapterDetail) {
            title.text = chapter.title
            date.text = chapter.date
        }
    }
}
