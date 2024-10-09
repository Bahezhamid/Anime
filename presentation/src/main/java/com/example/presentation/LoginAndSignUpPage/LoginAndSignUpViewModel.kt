package com.example.presentation.LoginAndSignUpPage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginAndSignUpViewModel @Inject constructor(
   private val userPreferencesRepository: UserPreferencesRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginAndSignUpUiState())
    val uiState: StateFlow<LoginAndSignUpUiState> get() = _uiState.asStateFlow()

    private val _loginUiState = MutableStateFlow(UsersUiState())
    val loginUiState : StateFlow<UsersUiState> get() = _loginUiState.asStateFlow()
    init {

        viewModelScope.launch {
            userPreferencesRepository.getUserEmail().collect { savedEmail ->
                userPreferencesRepository.getUserPassword().collect { savedPassword ->
                    if (!savedEmail.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
                        login(savedEmail, savedPassword)
                    } else {
                        _loginUiState.value = _loginUiState.value.copy(isLoading = false)
                    }
                }
            }
        }
    }
    fun signOut() {
        viewModelScope.launch {
            try {
                authRepository.signOut()
                _loginUiState.value = UsersUiState()
                _uiState.value = LoginAndSignUpUiState(
                    email = "",
                    password = "",
                    isRememberMeOn = false,
                    confirmPassword = "",
                    userName = "",
                )
                userPreferencesRepository.storeUserPreferences("", "")
            }catch (e : Exception) {
                Log.d("singOut" , "faild to Sign Out")
            }
        }
    }
    fun updateEmailTextFieldValue(newValue: String) {
        _uiState.value = _uiState.value.copy(email = newValue)
    }
    fun updateUserNameTextFieldValue(newValue: String) {
        _uiState.value = _uiState.value.copy(userName = newValue)
    }

    fun updatePasswordTextFieldValue(newValue: String) {
        _uiState.value = _uiState.value.copy(password = newValue)
    }

    fun updateConfirmPasswordTextFieldValue(newValue: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = newValue)
    }

    fun updateRememberMeValue(rememberMe: Boolean) {
        _uiState.value = _uiState.value.copy(isRememberMeOn = rememberMe)
    }

    suspend fun saveAccount(signUpState: LoginAndSignUpUiState) {
        if (validateInput(signUpState)) {
            _loginUiState.value = _loginUiState.value.copy(
                isLoading = true
            )
            try {

                val authResult = withContext(Dispatchers.IO) {
                   authRepository.signUp(signUpState.email, signUpState.password ,signUpState.userName)
                }
                if (authResult.userId != "") {
                    _loginUiState.value = UsersUiState(
                        email = authResult.email,
                        userName = authResult.userName,
                        isSuccess = authResult.isSuccess,
                        isLoading = false,
                        userid = authResult.userId
                    )
                    if (_uiState.value.isRememberMeOn) {
                        viewModelScope.launch {
                            userPreferencesRepository.storeUserPreferences(signUpState.email, signUpState.password)
                        }
                    }
                }
            } catch (e: Exception) {

                _uiState.value = _uiState.value.copy(
                    emailError = "Not Valid Email"
                )
                _loginUiState.value = _loginUiState.value.copy(
                    isSuccess = false,
                    isLoading = false
                )
            }
        }
    }
    suspend fun login(email: String, password: String) {
        _loginUiState.value = _loginUiState.value.copy(
            isLoading = true
        )
        try {
            val authResult = withContext(Dispatchers.IO) {
                authRepository.login(email, password)
            }
            if (authResult.userId != "") {

                    _loginUiState.value = UsersUiState(
                        email = authResult.email,
                        userName = authResult.userName,
                        isSuccess = authResult.isSuccess,
                        isLoading = false,
                        userid = authResult.userId
                    )
                    if (_uiState.value.isRememberMeOn) {
                        viewModelScope.launch {
                           userPreferencesRepository.storeUserPreferences(email , password)
                        }
                    }
                } else {
                    _loginUiState.value = _loginUiState.value.copy(
                        errorMessage = "Wrong Email Or Password",
                        isSuccess = false,
                        isLoading = false
                    )
                }
        } catch (e: Exception) {
            _loginUiState.value = _loginUiState.value.copy(
                errorMessage = "Wrong Email Or Password",
                isSuccess = false,
                isLoading = false
            )
        }
    }
    suspend fun forgetPassword(signUpState: LoginAndSignUpUiState) {
        if(  validateInput(signUpState = signUpState, isForgetPassword = true)) {
            _loginUiState.value = _loginUiState.value.copy(
                isLoading = true
            )
        val result  =  authRepository.forgetPassword(signUpState.email)
                    if (result) {
                        _loginUiState.value = _loginUiState.value.copy(
                            isLoading = false,
                            isPasswordResetSent = true
                        )

                    } else {
                        _loginUiState.value = _loginUiState.value.copy(
                            isSuccess = false,
                            isLoading = false,
                            isPasswordResetSent = false,
                            errorMessage = "Wrong Email"
                        )
                    }

        }
    }
    private fun validateInput(signUpState: LoginAndSignUpUiState ,isForgetPassword : Boolean = false): Boolean {

        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=])(?=\\S+\$).{8,}\$"
        if (isForgetPassword) {
            val isEmailValid = signUpState.email.matches(Regex(emailPattern))
            var emailError : String? = null
            if (!isEmailValid) {
                emailError = "Invalid email format."
            }
            _uiState.value = _uiState.value.copy(
                emailError = emailError,
            )

            return isEmailValid
        } else {
            val isEmailValid = signUpState.email.matches(Regex(emailPattern))
            val isPasswordValid = signUpState.password.matches(Regex(passwordPattern))
            val doPasswordsMatch = signUpState.password == signUpState.confirmPassword


            var emailError: String? = null
            var passwordError: String? = null
            var confirmPasswordError: String? = null
            var emptyUserName: String? = null
            if (!isEmailValid) {
                emailError = "Invalid email format."
            }

            if (!isPasswordValid) {
                passwordError =
                    "Password must be at least 8 characters, include an upper case letter, a number, and a special character."
            }

            if (!doPasswordsMatch) {
                confirmPasswordError = "Passwords do not match."
            }
            if (signUpState.userName == "") {
                emptyUserName = "Please Enter Your Username."
            }
            _uiState.value = _uiState.value.copy(
                emailError = emailError,
                passwordError = passwordError,
                confirmPasswordError = confirmPasswordError,
                userNameError = emptyUserName
            )

            return isEmailValid && isPasswordValid && doPasswordsMatch && signUpState.userName != ""
        }

    }
}
