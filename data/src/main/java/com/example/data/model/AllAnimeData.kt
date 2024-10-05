package com.example.data.model
import com.google.gson.annotations.SerializedName

data class AllAnimeData(
    @SerializedName("data")
    val data: MutableList<Data?> = mutableListOf(),
    @SerializedName("pagination")
    val pagination: Pagination? = Pagination()
)