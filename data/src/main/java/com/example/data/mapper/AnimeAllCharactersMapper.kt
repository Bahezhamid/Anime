package com.example.data.mapper

import com.example.data.model.Data
import com.example.domain.entity.AllGenreData
import com.example.domain.entity.AnimeAllCharactersData

fun Data.toAnimeAllCharactersData() : AnimeAllCharactersData {
    return AnimeAllCharactersData(
        id = malId,
        imageUrl = images?.jpg?.imageUrl
    )
}

fun List<Data?>.toListOfAnimeAllCharactersData(): List<AnimeAllCharactersData?> {
    return this.map {
        it?.toAnimeAllCharactersData()
    }
}