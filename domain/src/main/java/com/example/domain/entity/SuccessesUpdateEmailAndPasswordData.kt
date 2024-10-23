package com.example.domain.entity

data class SuccessesUpdateEmailAndPasswordData(
    val isLoading : Boolean,
    val isSuccess : Boolean,
    val errorMessage : String = ""
)
