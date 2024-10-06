package com.example.data.mapper

import com.example.data.model.Data
import com.example.domain.entity.AllGenreData
import com.example.domain.entity.AnimeData

fun Data.toGenre() : AllGenreData {
    return AllGenreData(
        id = malId,
        title = name,
        url = url
    )
}

fun List<Data?>.toGenres(): List<AllGenreData?> {
    return this.map {
        it?.toGenre()
    }
}