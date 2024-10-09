package com.example.domain.entity

data class UsersData(
    val userId : String ="",
    val email : String = "",
    val userName : String = "",
    val errorMessage : String ="",
    val isSuccess : Boolean
)
