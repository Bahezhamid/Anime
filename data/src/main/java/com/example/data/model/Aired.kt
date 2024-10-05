package com.example.data.model
import com.google.gson.annotations.SerializedName

data class Aired(
    @SerializedName("from")
    val from: String? = null,
    @SerializedName("prop")
    val prop: Prop? = null,
    @SerializedName("string")
    val string: String? = null,
    @SerializedName("to")
    val to: String? = null
)