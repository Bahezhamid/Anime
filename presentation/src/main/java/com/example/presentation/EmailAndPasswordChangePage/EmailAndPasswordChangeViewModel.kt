package com.example.presentation.EmailAndPasswordChangePage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.UpdateEmailData
import com.example.domain.entity.UpdatePasswordData
import com.example.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmailAndPasswordChangeViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _emailAndPasswordChangeUiState = MutableStateFlow(EmailAndPasswordChangeUiState())
    val emailAndPasswordChangeUiState: StateFlow<EmailAndPasswordChangeUiState> get() = _emailAndPasswordChangeUiState.asStateFlow()
    fun updateNewEmailTextFieldValue(newValue: String) {
        _emailAndPasswordChangeUiState.value = _emailAndPasswordChangeUiState.value.copy(newEmail = newValue)
    }
    fun updateOldEmailTextFieldValue(newValue: String) {
        _emailAndPasswordChangeUiState.value = _emailAndPasswordChangeUiState.value.copy(oldEmail = newValue)
    }
    fun updateConfirmEmailTextFieldValue(newValue: String) {
        _emailAndPasswordChangeUiState.value = _emailAndPasswordChangeUiState.value.copy(confirmEmail = newValue)
    }
    fun updatePasswordTextFieldValue(newValue: String) {
        _emailAndPasswordChangeUiState.value = _emailAndPasswordChangeUiState.value.copy(password = newValue)
    }
    fun updateNewPasswordTextFieldValue(newValue: String) {
        _emailAndPasswordChangeUiState.value = _emailAndPasswordChangeUiState.value.copy(newPassword = newValue)
    }
    fun updateOldPasswordTextFieldValue(newValue: String) {
        _emailAndPasswordChangeUiState.value = _emailAndPasswordChangeUiState.value.copy(oldPassword = newValue)
    }
    fun updateConfirmPasswordTextFieldValue(newValue: String) {
        _emailAndPasswordChangeUiState.value = _emailAndPasswordChangeUiState.value.copy(confirmPassword = newValue)
    }
    fun SignOut() {
        _emailAndPasswordChangeUiState.value = EmailAndPasswordChangeUiState()
    }
    fun updateEmail(
        emailAndPasswordChangeUiState: EmailAndPasswordChangeUiState,
        password: String
    ) {
        if (validateInput(emailAndPasswordChangeUiState, false)) {
            _emailAndPasswordChangeUiState.value = _emailAndPasswordChangeUiState.value.copy(isLoading = true)
            val updateEmailData = UpdateEmailData(
                newEmail = emailAndPasswordChangeUiState.newEmail,
                oldEmail = emailAndPasswordChangeUiState.oldEmail,
                password = password
            )
            viewModelScope.launch {
              val result =  authRepository.updateEmailAddress(updateEmailData = updateEmailData)
                _emailAndPasswordChangeUiState.value = _emailAndPasswordChangeUiState.value.copy(
                    isSuccess = result.isSuccess,
                    isLoading = result.isLoading,
                    passwordError = result.errorMessage
                )
            }
        } else {

            _emailAndPasswordChangeUiState.value = _emailAndPasswordChangeUiState.value.copy(
                currentEmailError = "Invalid input"
            )
        }
    }


    fun updatePassword(
        emailAndPasswordChangeUiState: EmailAndPasswordChangeUiState,
    ) {
        if (validateInput(emailAndPasswordChangeUiState, true)) {
            _emailAndPasswordChangeUiState.value = _emailAndPasswordChangeUiState.value.copy(isLoading = true)
            val updatePasswordData = UpdatePasswordData(
               newPassword = emailAndPasswordChangeUiState.newPassword,
                oldPassword = emailAndPasswordChangeUiState.oldPassword
            )
         viewModelScope.launch {
             val result = authRepository.updatePassword(updatePasswordData = updatePasswordData)
             _emailAndPasswordChangeUiState.value = _emailAndPasswordChangeUiState.value.copy(
                 isLoading = result.isLoading,
                 isSuccess = result.isSuccess,
                 passwordError = result.errorMessage
             )
         }
        } else {
            _emailAndPasswordChangeUiState.value = _emailAndPasswordChangeUiState.value.copy(
                currentPasswordError = "Invalid input"
            )
        }
    }




    private fun validateInput(emailAndPasswordChangeUiState: EmailAndPasswordChangeUiState, iaPasswordChange : Boolean): Boolean {
        if(iaPasswordChange){
            val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=])(?=\\S+\$).{8,}\$"
            val isPasswordValid = emailAndPasswordChangeUiState.newPassword.matches(Regex(passwordPattern))
            val doPasswordsMatch = emailAndPasswordChangeUiState.newPassword== emailAndPasswordChangeUiState.confirmPassword
            var passwordError: String? = null
            var confirmPasswordError: String? = null
            if (!isPasswordValid) {
                passwordError = "Password must be at least 8 characters, include an upper case letter, a number, and a special character."
            }
            if (!doPasswordsMatch) {
                confirmPasswordError = "Passwords do not match."
            }
            _emailAndPasswordChangeUiState.value = _emailAndPasswordChangeUiState.value.copy(
                passwordError = passwordError,
                confirmPasswordError = confirmPasswordError
            )
            return  isPasswordValid && doPasswordsMatch
        }
        else {

            val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
            var confirmEmailError : String? = null

            val isEmailValid = emailAndPasswordChangeUiState.newEmail.matches(Regex(emailPattern))

            val doEmailsMatch =
                emailAndPasswordChangeUiState.newEmail == emailAndPasswordChangeUiState.confirmEmail

            var emailError: String? = null
            if (!isEmailValid) {
                emailError = "Invalid email format."
            }
            if (!doEmailsMatch) {
                confirmEmailError = "Emails do not match."
            }

            _emailAndPasswordChangeUiState.value = _emailAndPasswordChangeUiState.value.copy(
                emailError = emailError,
                confirmEmailError = confirmEmailError
            )
            return isEmailValid && doEmailsMatch
        }
    }

}
