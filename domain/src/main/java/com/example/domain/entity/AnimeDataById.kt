package com.example.domain.entity

data class AnimeDataById(
    val id: Int?,
    val title: String?,
    val imageUrl: String?,
    val trailerImg : String?,
    val trailerUrl : String?,
    val releaseDate : String?,
    val popularityRank : Int?,
    val globalRank : Int?,
    val genre : List<String?>,
    val episodes : Int?,
    val episodesMin : String?,
    val description : String?
)
