package com.example.data.model
import com.google.gson.annotations.SerializedName

data class Prop(
    @SerializedName("from")
    val from: From? = null,
    @SerializedName("to")
    val to: To? = null
)