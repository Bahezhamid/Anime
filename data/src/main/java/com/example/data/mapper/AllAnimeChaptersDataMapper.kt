package com.example.data.mapper

import com.example.data.model.AnimeChapters
import com.example.data.model.Data
import com.example.domain.entity.AllAnimeChaptersData
import com.example.domain.entity.AnimeChaptersData

fun Data.toAnimeChapters() : AnimeChaptersData {
    return AnimeChaptersData(
        id = malId,
        imageUrl = images?.jpg?.imageUrl,
        title = title,
        episodeNumber = episode,
        url = url
    )
}
fun AnimeChapters.toAllAnimeChaptersData() : AllAnimeChaptersData {
    return AllAnimeChaptersData(
        page = pagination?.lastVisiblePage,
        listOfChapters = data?.map {
            it.toAnimeChapters()
        }
    )
}