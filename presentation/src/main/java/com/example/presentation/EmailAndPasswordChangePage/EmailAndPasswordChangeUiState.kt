package com.example.presentation.EmailAndPasswordChangePage

data class EmailAndPasswordChangeUiState(
    val oldEmail: String = "",
    val newEmail: String = "",
    val confirmEmail : String ="",
    val password : String = "",
    val oldPassword: String = "",
    val newPassword: String ="",
    val currentEmailError : String? = "",
    val confirmPassword: String = "",
    val emailError : String? = "",
    val confirmEmailError: String? = "",
    val passwordError : String? = "",
    val confirmPasswordError : String? ="",
    val currentPasswordError : String? = "",
    val isLoading : Boolean = false,
    val isSuccess : Boolean = false
)
