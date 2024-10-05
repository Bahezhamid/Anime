package com.example.data.model
import com.google.gson.annotations.SerializedName

data class AnimeCharacters(
    @SerializedName("data")
    val `data`: List<Data>? = listOf()
)