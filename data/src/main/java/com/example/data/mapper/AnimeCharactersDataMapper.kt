package com.example.data.mapper

import com.example.data.model.Anime
import com.example.data.model.Data
import com.example.data.model.Voice
import com.example.domain.entity.AnimeCharactersData
import com.example.domain.entity.CharactersAnime
import com.example.domain.entity.CharactersVoiceActor

fun Data.toCharactersData() : AnimeCharactersData {
    return  AnimeCharactersData(
        id = malId,
        name = name,
        imageUrl = images?.jpg?.imageUrl,
        description = about,
        listOfAnime = anime?.toCharactersAnime(),
        listOfVoiceActor = voices?.toCharactersVoiceActor()
    )
}
fun List<Anime?>.toCharactersAnime() : List<CharactersAnime?> {
    return this.map {
        CharactersAnime(
            id = it?.anime?.malId,
            poster = it?.anime?.images?.jpg?.imageUrl,
            role = it?.role,
            title = it?.anime?.title
        )
    }
}

fun List<Voice>.toCharactersVoiceActor() : List<CharactersVoiceActor?> {
    return this.map {
        CharactersVoiceActor(
            name = it.person?.name,
            language =it.language ,
            image = it.person?.images?.jpg?.imageUrl ,
        )
    }
}