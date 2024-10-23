package com.example.domain.entity

data class UpdatePasswordData(
    val newPassword : String,
    val oldPassword: String
)
