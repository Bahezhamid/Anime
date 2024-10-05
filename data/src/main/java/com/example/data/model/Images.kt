package com.example.data.model
import com.google.gson.annotations.SerializedName

data class Images(
    @SerializedName("jpg")
    val jpg: Jpg? = null,
    @SerializedName("webp")
    val webp: Webp? = null,
    @SerializedName("image_url")
    val imageUrl: String? = null,
    @SerializedName("large_image_url")
    val largeImageUrl: String? = null,
    @SerializedName("maximum_image_url")
    val maximumImageUrl: String? = null,
    @SerializedName("medium_image_url")
    val mediumImageUrl: String? = null,
    @SerializedName("small_image_url")
    val smallImageUrl: String? = null
)