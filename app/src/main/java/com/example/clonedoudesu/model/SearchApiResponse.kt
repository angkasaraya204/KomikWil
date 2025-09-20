package com.example.clonedoudesu.model

import com.google.gson.annotations.SerializedName

// Data class untuk menampung seluruh respons dari API /search
data class SearchApiResponse(
    @SerializedName("status")
    val status: Boolean?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("keyword")
    val keyword: String?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("total")
    val total: Int?,
    @SerializedName("data")
    val data: List<SearchResultItem>?
)

// Data class untuk merepresentasikan satu item hasil pencarian DARI API /search
data class SearchResultItem(
    @SerializedName("title")
    val title: String?,
    @SerializedName("altTitle")
    val altTitle: String?,
    @SerializedName("slug") // API mengembalikan "slug", tapi di contoh lain kita pakai "href" untuk membuat slug internal
    val apiSlug: String?, // Kita akan pakai ini sebagai slug internal jika ada, atau proses dari href
    @SerializedName("href") // "/detail-komik/alone-leveling-ragnarok/"
    val href: String?,
    @SerializedName("thumbnail")
    val thumbnail: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("genre")
    val genre: String?,
    @SerializedName("description")
    val description: String?
)
