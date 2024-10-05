package com.example.data.model
import com.google.gson.annotations.SerializedName

data class Theme(
    @SerializedName("mal_id")
    val malId: Int? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("url")
    val url: String? = null
)