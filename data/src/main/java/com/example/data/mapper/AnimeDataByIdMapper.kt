package com.example.data.mapper

import com.example.data.model.Data
import com.example.domain.entity.AnimeDataById

fun Data.toAnimeDataById() : AnimeDataById {
    return com.example.domain.entity.AnimeDataById(
        id = malId,
        imageUrl = images?.jpg?.largeImageUrl,
        title = titleEnglish,
        trailerImg = trailer?.images?.largeImageUrl,
        trailerUrl = trailer?.url,
        releaseDate = aired?.from,
        popularityRank = popularity,
        globalRank = rank,
        genre =genres?.map { it.name } ?: emptyList(),
        episodes = episodes,
        episodesMin = duration,
        description = synopsis
    )
}