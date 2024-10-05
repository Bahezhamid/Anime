package com.example.data.model
import com.google.gson.annotations.SerializedName

data class Pagination(
    @SerializedName("current_page")
    val currentPage: Int? = null,
    @SerializedName("has_next_page")
    val hasNextPage: Boolean? = null,
    @SerializedName("items")
    val items: Items? = null,
    @SerializedName("last_visible_page")
    val lastVisiblePage: Int? = null
)