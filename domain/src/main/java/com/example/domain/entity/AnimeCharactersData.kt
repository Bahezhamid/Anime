package com.example.domain.entity

data class AnimeCharactersData(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val description : String,
    val voiceActorName : String,
    val voiceActorImageUrl : String,
    val voiceActorLanguage: String
)