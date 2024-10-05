package com.example.data.model
import com.google.gson.annotations.SerializedName

data class Manga(
    @SerializedName("manga")
    val manga: MangaX? = MangaX(),
    @SerializedName("role")
    val role: String? = ""
)