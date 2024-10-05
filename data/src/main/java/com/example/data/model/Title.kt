package com.example.data.model

import com.google.gson.annotations.SerializedName

data class Title(
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("type")
    val type: String? = null
)