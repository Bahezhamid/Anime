package com.example.anime.di

import com.example.data.network.AnimeApiService
import com.example.data.repository.NetworkAnimeDataRepository
import com.example.domain.repository.AnimeDataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAndroidApi() : AnimeApiService {
        val baseUrl = "https://api.jikan.moe/v4/"
         val client = OkHttpClient.Builder()
            .connectTimeout(40, TimeUnit.SECONDS)
            .writeTimeout(40, TimeUnit.SECONDS)
            .readTimeout(40, TimeUnit.SECONDS)
            .build()
         val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return  retrofit.create(AnimeApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAnimeDataRepository(apiService: AnimeApiService) : AnimeDataRepository {
        return NetworkAnimeDataRepository(apiService)
    }
}