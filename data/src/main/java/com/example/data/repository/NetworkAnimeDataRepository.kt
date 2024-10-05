package com.example.data.repository

import com.example.data.mapper.toEntity
import com.example.data.network.AnimeApiService
import com.example.domain.entity.AnimeDataWithPage
import com.example.domain.repository.AnimeDataRepository

abstract class NetworkAnimeDataRepository(
    private val animeApiService: AnimeApiService
) : AnimeDataRepository {
    override suspend fun getAnimeData(page: Int): AnimeDataWithPage? = animeApiService.getAnimeData(page = page)?.toEntity()
}