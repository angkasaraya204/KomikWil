package com.example.clonedoudesu.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import com.example.clonedoudesu.model.Komik // Tetap dibutuhkan untuk endpoint lain
import com.example.clonedoudesu.model.KomikDetail
import com.example.clonedoudesu.model.GenreDetail
import com.example.clonedoudesu.model.Chapter
import com.example.clonedoudesu.model.PustakaApiResponse
import com.example.clonedoudesu.model.BerwarnaApiResponse
import com.example.clonedoudesu.model.KomikPopulerApiResponse
import com.example.clonedoudesu.model.SearchApiResponse // Import data class baru untuk Search

interface KomikuApiService {
    @GET("rekomendasi")
    suspend fun getRekomendasi(): List<Komik>

    // Fungsi getTrending() sudah dihapus sebelumnya

    @GET("terbaru-2")
    suspend fun getTerbaru2(): List<Komik>

    // Endpoint "terbaru" (Update Terbaru) sudah dihapus sebelumnya

    @GET("pustaka")
    suspend fun getPustaka(): PustakaApiResponse

    @GET("berwarna")
    suspend fun getBerwarna(): BerwarnaApiResponse

    @GET("komik-populer")
    suspend fun getKomikPopuler(): KomikPopulerApiResponse

    @GET("detail-komik/{slug}")
    suspend fun getDetailKomik(@Path("slug") slug: String): KomikDetail

    @GET("baca-chapter/{slug}/{chapter}")
    suspend fun getBacaChapter(
        @Path("slug") slug: String,
        @Path("chapter") chapter: String
    ): Chapter

    // Mengubah return type untuk searchKomik()
    @GET("search")
    suspend fun searchKomik(@Query("q") keyword: String): SearchApiResponse // <--- PERUBAHAN DI SINI

    @GET("genre-detail/{slug}")
    suspend fun getGenreDetail(@Path("slug") slug: String): GenreDetail
}

