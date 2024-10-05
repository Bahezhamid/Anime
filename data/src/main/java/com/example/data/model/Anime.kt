package com.example.data.model
import com.google.gson.annotations.SerializedName

data class Anime(
    @SerializedName("anime")
    val anime: AnimeXX? = AnimeXX(),
    @SerializedName("role")
    val role: String? = ""
)