package com.example.data.model

import com.google.gson.annotations.SerializedName

data class AnimeChapters(
    @SerializedName("data")
    val `data`: List<Data>? = listOf(),
    @SerializedName("pagination")
    val pagination: Pagination? = Pagination()
)