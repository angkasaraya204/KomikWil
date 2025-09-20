package com.example.clonedoudesu.model

import com.google.gson.annotations.SerializedName

// Data class untuk menampung seluruh respons dari API /berwarna
data class BerwarnaApiResponse(
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: BerwarnaData // Objek data yang berisi page dan results
)

// Data class untuk objek "data" di dalam BerwarnaApiResponse
data class BerwarnaData(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<KomikItemFromApi> // Bisa menggunakan kembali KomikItemFromApi
)

// KomikItemFromApi sudah didefinisikan di PustakaApiResponse.kt
// Jika belum atau terpisah, bisa didefinisikan ulang di sini atau pastikan di-import dengan benar.
// data class KomikItemFromApi(
// @SerializedName("title")
// val title: String?,
// @SerializedName("thumbnail")
// val thumbnail: String?,
// @SerializedName("detailUrl")
// val detailUrl: String?,
// ... field lainnya
// )
