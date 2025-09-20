package com.example.clonedoudesu.model

import com.google.gson.annotations.SerializedName

// Data class untuk menampung seluruh respons dari API /pustaka
data class PustakaApiResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<KomikItemFromApi> // Menggunakan KomikItemFromApi untuk item di dalam results
)

// Data class untuk merepresentasikan satu item komik SEPERTI YANG ADA DI JSON API
// Ini mungkin memiliki field yang berbeda dari Komik.kt internal Anda
data class KomikItemFromApi(
    @SerializedName("title")
    val title: String?,
    @SerializedName("thumbnail")
    val thumbnail: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("genre")
    val genre: String?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("detailUrl")
    val detailUrl: String?, // Ini akan menjadi 'slug' untuk Komik.kt kita
    @SerializedName("description")
    val description: String?,
    @SerializedName("stats")
    val stats: String?,
    // Jika ada field lain seperti firstChapter, latestChapter, bisa ditambahkan di sini
    // Namun untuk ditampilkan di MainActivity, mungkin hanya title, thumbnail, dan detailUrl (slug) yang utama
)
