package com.example.data.DataSource

import android.content.Context
import com.example.data.network.AnimeApiService
import com.example.data.repository.NetworkAnimeDataRepository
import com.example.domain.repository.AnimeDataRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

interface DataSource {
    val animeDataRepository : AnimeDataRepository
}
class AnimeDataSource(private val context: Context) : DataSource {
    private val baseUrl = "https://api.jikan.moe/v4/"
    private val client = OkHttpClient.Builder()
        .connectTimeout(40, TimeUnit.SECONDS)
        .writeTimeout(40, TimeUnit.SECONDS)
        .readTimeout(40, TimeUnit.SECONDS)
        .build()
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitService: AnimeApiService by lazy {
        retrofit.create(AnimeApiService::class.java)
    }
    override val animeDataRepository: AnimeDataRepository by lazy {
        NetworkAnimeDataRepository(retrofitService)
    }
}