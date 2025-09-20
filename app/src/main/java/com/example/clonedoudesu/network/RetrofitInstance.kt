package com.example.clonedoudesu.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://project-test-angkasa-raya.cobaproject.my.id"

    val apiService: KomikuApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KomikuApiService::class.java)
    }
}
