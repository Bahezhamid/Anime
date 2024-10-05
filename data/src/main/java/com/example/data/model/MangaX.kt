package com.example.data.model
import com.google.gson.annotations.SerializedName

data class MangaX(
    @SerializedName("manga")
    val manga: MangaXX? = MangaXX(),
    @SerializedName("role")
    val role: String? = ""
)