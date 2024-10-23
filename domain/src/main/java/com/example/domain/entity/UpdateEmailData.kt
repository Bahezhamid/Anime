package com.example.domain.entity

data class UpdateEmailData(
    val newEmail : String,
    val oldEmail : String,
    val password : String,
    )
