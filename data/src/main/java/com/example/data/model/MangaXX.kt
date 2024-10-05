package com.example.data.model
import com.google.gson.annotations.SerializedName

data class MangaXX(
    @SerializedName("images")
    val images: Images? = Images(),
    @SerializedName("mal_id")
    val malId: Int? = 0,
    @SerializedName("title")
    val title: String? = "",
    @SerializedName("url")
    val url: String? = ""
)