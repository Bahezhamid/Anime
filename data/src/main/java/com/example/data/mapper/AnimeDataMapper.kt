package com.example.data.mapper
import com.example.data.model.AllAnimeData
import com.example.data.model.Data
import com.example.domain.entity.AnimeData
import com.example.domain.entity.AnimeDataWithPage

fun Data.AnimeData(): AnimeData {
    return AnimeData(
        id = this.malId,
        title = this.title,
        imageUrl = this.images?.jpg?.imageUrl
    )
}

fun AllAnimeData.toEntity() : AnimeDataWithPage {
    return AnimeDataWithPage(
        page = pagination?.lastVisiblePage,
        animeData = data.map {
            it?.AnimeData()
        }
        )
}