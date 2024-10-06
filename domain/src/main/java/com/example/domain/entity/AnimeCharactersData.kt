package com.example.domain.entity

data class AnimeCharactersData(
    val id: Int?,
    val name: String?,
    val imageUrl: String?,
    val description : String?,
    val listOfAnime : List<CharactersAnime?>?,
    val listOfVoiceActor : List<CharactersVoiceActor?>?
)