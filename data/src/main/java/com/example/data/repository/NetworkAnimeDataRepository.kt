package com.example.data.repository

import com.example.data.mapper.toAllAnimeChaptersData
import com.example.data.mapper.toAnimeDataById
import com.example.data.mapper.toCharactersData
import com.example.data.mapper.toEntity
import com.example.data.mapper.toGenres
import com.example.data.mapper.toListOfAnimeAllCharactersData
import com.example.data.network.AnimeApiService
import com.example.domain.entity.AllAnimeChaptersData
import com.example.domain.entity.AllGenreData
import com.example.domain.entity.AnimeAllCharactersData
import com.example.domain.entity.AnimeCharactersData
import com.example.domain.entity.AnimeDataById
import com.example.domain.entity.AnimeDataWithPage
import com.example.domain.repository.AnimeDataRepository

class NetworkAnimeDataRepository(
    private val animeApiService: AnimeApiService
) : AnimeDataRepository {
    override suspend fun getAnimeData(page: Int): AnimeDataWithPage? = animeApiService.getAnimeData(page = page)?.toEntity()
    override suspend fun getAnimeDataById(id: Int): AnimeDataById? = animeApiService.getAnimeDataById(id)?.data?.toAnimeDataById()
    override suspend fun getAllGenres(): List<AllGenreData?>? = animeApiService.getAllGenres()?.data?.toGenres()
    override suspend fun getAllCharacters(id: Int): List<AnimeAllCharactersData?>? = animeApiService.getCharacters(id = id)?.data?.toListOfAnimeAllCharactersData()
    override suspend fun getAnimeChapters(id: Int, page: Int): AllAnimeChaptersData = animeApiService.getAnimeChapters(id = id,page = page).toAllAnimeChaptersData()
    override suspend fun getAllCharactersData(id: Int): AnimeCharactersData? =animeApiService.getCharactersData(id= id).data?.toCharactersData()
    override suspend fun getAnimeByScore(minScore: Int): AnimeDataWithPage = animeApiService.getAnimeByScore(minScore=minScore).toEntity()
    override suspend fun getAnimeByName(animeName: String): AnimeDataWithPage = animeApiService.getAnimeByName(animeName = animeName).toEntity()
    override suspend fun getAnimeByGenre(genreId: Int,page: Int): AnimeDataWithPage = animeApiService.getAnimeByGenre(genreId= genreId, page = page).toEntity()
}