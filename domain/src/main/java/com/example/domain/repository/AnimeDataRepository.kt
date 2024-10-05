package com.example.domain.repository

import com.example.domain.entity.AllGenreData
import com.example.domain.entity.AnimeAllCharactersData
import com.example.domain.entity.AnimeChaptersData
import com.example.domain.entity.AnimeCharactersData
import com.example.domain.entity.AnimeData
import com.example.domain.entity.AnimeDataById
import com.example.domain.entity.AnimeDataWithPage

interface AnimeDataRepository {
    suspend fun getAnimeData(page: Int): AnimeDataWithPage?
    suspend fun getAnimeDataById(id : Int) : AnimeDataById?
    suspend fun getAllGenres() : AllGenreData?
    suspend fun getAllCharacters(id : Int) : AnimeAllCharactersData?
    suspend fun getAnimeChapters(id : Int, page: Int) : AnimeChaptersData?
    suspend fun getAllCharactersData(id : Int) : AnimeCharactersData
    suspend fun getAnimeByScore(minScore : Int) : AnimeData?
    suspend fun getAnimeByName(animeName : String) : AnimeData
    suspend fun getAnimeByGenre(genreId : Int ,page: Int) : AnimeData
}