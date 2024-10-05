package com.example.domain.entity

data class AnimeDataById(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val trailerImg : String,
    val trailerUrl : String,
    val releaseDate : String,
    val popularityRank : String,
    val globalRank : String,
    val genre : String,
    val episodes : String,
    val episodesMin : String,
    val description : String
)
