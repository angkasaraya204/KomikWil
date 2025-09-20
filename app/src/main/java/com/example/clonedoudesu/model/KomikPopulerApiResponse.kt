package com.example.clonedoudesu.model

import com.google.gson.annotations.SerializedName

// Data class untuk menampung seluruh respons dari API /komik-populer
data class KomikPopulerApiResponse(
    @SerializedName("manga")
    val manga: PopulerCategory?,
    @SerializedName("manhwa")
    val manhwa: PopulerCategory?,
    @SerializedName("manhua")
    val manhua: PopulerCategory?
)

// Data class untuk setiap kategori (manga, manhwa, manhua)
data class PopulerCategory(
    @SerializedName("title")
    val categoryTitle: String?,
    @SerializedName("items")
    val items: List<PopulerKomikItem>?
)

// Data class untuk merepresentasikan satu item komik DARI API /komik-populer
data class PopulerKomikItem(
    @SerializedName("title")
    val title: String?,
    @SerializedName("thumbnail")
    val thumbnail: String?,
    @SerializedName("apiDetailLink") // Field ini berbeda dari KomikItemFromApi (detailUrl)
    val apiDetailLink: String?, // Ini akan menjadi 'slug' untuk Komik.kt kita
    @SerializedName("genre")
    val genre: String?,
    @SerializedName("latestChapter")
    val latestChapter: String?,
    // Tambahkan field lain jika perlu untuk mapping ke Komik.kt
    // atau jika ingin menampilkan lebih banyak info di UI nanti
)
