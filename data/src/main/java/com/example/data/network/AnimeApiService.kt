package com.example.data.network

import com.example.data.model.AllAnimeData
import com.example.data.model.AllGenres
import com.example.data.model.AnimeChapters
import com.example.data.model.AnimeCharacters
import com.example.data.model.AnimeDataById
import com.example.data.model.CharactersAllData
import com.example.domain.entity.AnimeData
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AnimeApiService {
    @GET("anime")
    suspend fun getAnimeData(@Query("page") page: Int): AllAnimeData?
    @GET("anime/{id}")
    suspend fun getAnimeDataById(@Path("id") id : Int) : AnimeDataById?
    @GET("genres/anime")
    suspend fun getAllGenres() :  AllGenres?
    @GET("anime/{id}/characters")
    suspend fun getCharacters(@Path("id") id  :Int) : AnimeCharacters?
    @GET("anime/{id}/videos/episodes")
    suspend fun getAnimeChapters(@Path("id") id : Int, @Query("page") page : Int) : AnimeChapters
    @GET("characters/{id}/full")
    suspend fun getCharactersData(@Path("id") id : Int) : CharactersAllData
    @GET("anime")
    suspend fun getAnimeByScore(
        @Query("min_score") minScore: Int,
        @Query("order_by") orderBy: String = "rank",
        @Query("limit") limit: Int = 10
    ): AnimeData
    @GET("anime")
    suspend fun getAnimeByName(@Query("q") animeName : String) : AnimeData
    @GET("anime")
    suspend fun getAnimeByGenre(@Query("genres")genreId : Int, @Query("page") page : Int) : AnimeData
}