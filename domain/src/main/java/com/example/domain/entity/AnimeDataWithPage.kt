package com.example.domain.entity

data class AnimeDataWithPage(
    val animeData: List<AnimeData?> = mutableListOf(),
    val page: Int?
)