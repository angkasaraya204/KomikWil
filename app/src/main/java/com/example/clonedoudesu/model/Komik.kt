package com.example.clonedoudesu.model

import com.google.gson.annotations.SerializedName

data class Komik(
    @SerializedName("title")
    val judul: String,

    // JSON punya "apiDetailLink" → kita mapping ke slug
    @SerializedName("apiDetailLink")
    val slug: String,

    val thumbnail: String
)
