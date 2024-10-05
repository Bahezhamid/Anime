package com.example.data.model
import com.google.gson.annotations.SerializedName

data class Items(
    @SerializedName("count")
    val count: Int? = null,
    @SerializedName("per_page")
    val perPage: Int? = null,
    @SerializedName("total")
    val total: Int? = null
)