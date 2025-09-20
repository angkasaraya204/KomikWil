package com.example.clonedoudesu.model

data class GenreDetail(
    val judul: String,
    val slug: String,
    val thumbnail: String,
    val komik: List<Komik>
)