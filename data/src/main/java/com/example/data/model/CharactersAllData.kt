package com.example.data.model

import com.google.gson.annotations.SerializedName

data class CharactersAllData(
    @SerializedName("data")
    val `data`: Data? = Data()
)