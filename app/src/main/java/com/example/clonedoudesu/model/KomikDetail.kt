package com.example.clonedoudesu.model

import com.google.gson.annotations.SerializedName

data class KomikDetail(
    val title: String = "",
    val alternativeTitle: String = "",
    val description: String = "",
    val sinopsis: String = "",
    val thumbnail: String = "",
    val info: Info? = null,
    val genres: List<String> = emptyList(),
    val slug: String = "",
    val firstChapter: ChapterInfo? = null,
    val latestChapter: ChapterInfo? = null,
    val chapters: List<ChapterDetail> = emptyList(),
    val similarKomik: List<SimilarKomik> = emptyList()
)

data class Info(
    @SerializedName("Judul Komik") val judulKomik: String = "",
    @SerializedName("Judul Indonesia") val judulIndonesia: String = "",
    @SerializedName("Jenis Komik") val jenisKomik: String = "",
    @SerializedName("Konsep Cerita") val konsepCerita: String = "",
    @SerializedName("Pengarang") val pengarang: String = "",
    @SerializedName("Status") val status: String = "",
    @SerializedName("Umur Pembaca") val umurPembaca: String = "",
    @SerializedName("Cara Baca") val caraBaca: String = ""
)

data class ChapterInfo(
    val title: String = "",
    val originalLink: String = "",
    val apiLink: String = "",
    val chapterNumber: String = ""
)

data class ChapterDetail(
    val title: String = "",
    val originalLink: String = "",
    val apiLink: String = "",
    val views: String = "",
    val date: String = "",
    val chapterNumber: String = ""
)

data class SimilarKomik(
    val title: String = "",
    val originalLink: String = "",
    val apiLink: String = "",
    val thumbnail: String = "",
    val type: String = "",
    val genres: String = "",
    val synopsis: String = "",
    val views: String = "",
    val slug: String = ""
)
