package com.example.data.model
import com.google.gson.annotations.SerializedName

data class AnimeDataById(
    @SerializedName("data")
    val `data`: Data? = Data()
)