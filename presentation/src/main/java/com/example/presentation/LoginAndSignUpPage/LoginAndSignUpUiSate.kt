package com.example.presentation.LoginAndSignUpPage
data class LoginAndSignUpUiState(
    val email: String = "",
    val userName: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isRememberMeOn: Boolean = true,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val userNameError : String? = null,

    )
