package com.example.data.model
import com.google.gson.annotations.SerializedName

data class Trailer(
    @SerializedName("embed_url")
    val embedUrl: String? = "",
    @SerializedName("images")
    val images: Images? = Images(),
    @SerializedName("url")
    val url: String? = "",
    @SerializedName("youtube_id")
    val youtubeId: String? = ""
)