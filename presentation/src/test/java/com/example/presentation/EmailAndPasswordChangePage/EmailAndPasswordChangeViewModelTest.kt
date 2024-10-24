package com.example.presentation.EmailAndPasswordChangePage

import com.example.domain.entity.SuccessesUpdateEmailAndPasswordData
import com.example.domain.entity.UpdateEmailData
import com.example.domain.entity.UpdatePasswordData
import com.example.domain.repository.AuthRepository
import com.example.presentation.LoginAndSignUpPage.LoginAndSignUpUiState
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

@ExperimentalCoroutinesApi
class EmailAndPasswordChangeViewModelTest {
    private lateinit var emailAndPasswordChangeViewModel: EmailAndPasswordChangeViewModel
    private val authRepository : AuthRepository = mockk()
    private val dispatcher  = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        emailAndPasswordChangeViewModel = EmailAndPasswordChangeViewModel(
            authRepository = authRepository
        )
    }

    @After
    fun tearsDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `updateNewEmailTextFieldValue fun Should update emailAndPasswordChangeUiState value` () {
        val newEmail = "Bahez@gmail.com"

        emailAndPasswordChangeViewModel.updateNewEmailTextFieldValue(newEmail)

        assertThat(emailAndPasswordChangeViewModel.emailAndPasswordChangeUiState.value.newEmail).isEqualTo(newEmail)
    }
    @Test
    fun `updateOldEmailTextFieldValue fun Should update emailAndPasswordChangeUiState value` () {
        val oldEmail = "Bahez@gmail.com"

        emailAndPasswordChangeViewModel.updateOldEmailTextFieldValue(oldEmail)

        assertThat(emailAndPasswordChangeViewModel.emailAndPasswordChangeUiState.value.oldEmail).isEqualTo(oldEmail)
    }
    @Test
    fun `updateConfirmEmailTextFieldValue fun Should update emailAndPasswordChangeUiState value` () {
        val confirmEmail = "Bahez@gmail.com"

        emailAndPasswordChangeViewModel.updateConfirmEmailTextFieldValue(confirmEmail)

        assertThat(emailAndPasswordChangeViewModel.emailAndPasswordChangeUiState.value.confirmEmail).isEqualTo(confirmEmail)
    }
    @Test
    fun `updatePasswordTextFieldValue fun Should update emailAndPasswordChangeUiState value` () {
        val password = "Bahez.151@"

        emailAndPasswordChangeViewModel.updatePasswordTextFieldValue(password)

        assertThat(emailAndPasswordChangeViewModel.emailAndPasswordChangeUiState.value.password).isEqualTo(password)
    }

    @Test
    fun `updateNewPasswordTextFieldValue fun Should update emailAndPasswordChangeUiState value` () {
        val newPassword = "Bahez.151@"

        emailAndPasswordChangeViewModel.updateNewPasswordTextFieldValue(newPassword)

        assertThat(emailAndPasswordChangeViewModel.emailAndPasswordChangeUiState.value.newPassword).isEqualTo(newPassword)
    }


    @Test
    fun `updateOldPasswordTextFieldValue fun Should update emailAndPasswordChangeUiState value` () {
        val oldPassword = "Bahez.151@"

        emailAndPasswordChangeViewModel.updateOldPasswordTextFieldValue(oldPassword)

        assertThat(emailAndPasswordChangeViewModel.emailAndPasswordChangeUiState.value.oldPassword).isEqualTo(oldPassword)
    }
    @Test
    fun `updateConfirmPasswordTextFieldValue fun Should update emailAndPasswordChangeUiState value` () {
        val confirmPassword = "Bahez.151@"

        emailAndPasswordChangeViewModel.updateConfirmPasswordTextFieldValue(confirmPassword)

        assertThat(emailAndPasswordChangeViewModel.emailAndPasswordChangeUiState.value.confirmPassword).isEqualTo(confirmPassword)
    }

    @Test
    fun ` SignOut fun should reset emailAndPasswordChangeUiState value` () {
        val emptyEmailAndPasswordChangeUiState = EmailAndPasswordChangeUiState()

        emailAndPasswordChangeViewModel.SignOut()

        assertThat(emailAndPasswordChangeViewModel.emailAndPasswordChangeUiState.value).isEqualTo(emptyEmailAndPasswordChangeUiState)
    }

    @Test
    fun `Valid email Input should update email field `() {
        val emailAndPasswordChangeUiState = EmailAndPasswordChangeUiState(
            newEmail = "bahezhamid00@gmail.com",
            oldEmail = "bahezhamid@gmail.com",
            confirmEmail = "bahezhamid00@gmail.com"
        )
        val updateEmailData = UpdateEmailData(
            newEmail = "bahezhamid00@gmail.com",
            oldEmail = "bahezhamid@gmail.com",
            password = "Bahez.151@"
        )
        val expectedDate = SuccessesUpdateEmailAndPasswordData(
            isLoading = false,
            isSuccess = true,
            errorMessage = ""
            )


        coEvery { authRepository.updateEmailAddress(updateEmailData = updateEmailData) } returns expectedDate
        emailAndPasswordChangeViewModel.updateEmail(emailAndPasswordChangeUiState =
        emailAndPasswordChangeUiState , password = "Bahez.151@")
        dispatcher.scheduler.advanceUntilIdle()


        assertThat(emailAndPasswordChangeViewModel.emailAndPasswordChangeUiState.value).isEqualTo(
            EmailAndPasswordChangeUiState(
                isLoading = expectedDate.isLoading,
                isSuccess = expectedDate.isSuccess,
                passwordError = expectedDate.errorMessage,
                emailError = null,
                confirmEmailError = null
            )
        )
        coVerify { authRepository.updateEmailAddress(updateEmailData = updateEmailData) }
    }

    @Test
    fun `Valid password Input should update password field` () {
        val emailAndPasswordChangeUiState = EmailAndPasswordChangeUiState(
            newPassword = "Bahez.151@",
            oldPassword = "Bahez.151@1",
            confirmPassword = "Bahez.151@"
        )
        val updatePasswordData = UpdatePasswordData(
            oldPassword = "Bahez.151@1",
            newPassword = "Bahez.151@"
        )
        val expectedDate = SuccessesUpdateEmailAndPasswordData(
            isLoading = false,
            isSuccess = true,
            errorMessage = ""
        )


        coEvery { authRepository.updatePassword(updatePasswordData = updatePasswordData) } returns expectedDate
        emailAndPasswordChangeViewModel.updatePassword(emailAndPasswordChangeUiState = emailAndPasswordChangeUiState)
        dispatcher.scheduler.advanceUntilIdle()


        assertThat(emailAndPasswordChangeViewModel.emailAndPasswordChangeUiState.value).isEqualTo(
            EmailAndPasswordChangeUiState(
                isLoading = expectedDate.isLoading,
                isSuccess = expectedDate.isSuccess,
                passwordError = expectedDate.errorMessage,
                confirmPasswordError = null
            )
        )
        coVerify { authRepository.updatePassword(updatePasswordData = updatePasswordData) }
    }

    @Test
    fun `empty Email Field should return error message` () = runTest {
        val expectedData = EmailAndPasswordChangeUiState(
            emailError = "Invalid email format.",
            currentEmailError = "Invalid input",
            confirmEmailError = "Emails do not match."

        )
        emailAndPasswordChangeViewModel.updateEmail(emailAndPasswordChangeUiState = EmailAndPasswordChangeUiState(
            newEmail = "",
            oldEmail = "bahezhamid00@gmail.com",
            confirmEmail = "bahezhamid00@gmail.com",
        ),
            password = ""
        )
        dispatcher.scheduler.advanceUntilIdle()
        assertThat(emailAndPasswordChangeViewModel.emailAndPasswordChangeUiState.value).isEqualTo(expectedData)
    }

    @Test
    fun `empty Password Field Should return Error Message` () = runTest {
        val expectedData = EmailAndPasswordChangeUiState(
            passwordError = "Password must be at least 8 characters, include an upper case letter, a number, and a special character.",
            confirmPasswordError = "Passwords do not match.",
            currentPasswordError = "Invalid input"
        )
        emailAndPasswordChangeViewModel.updatePassword(emailAndPasswordChangeUiState = EmailAndPasswordChangeUiState(
            password = "",
            confirmPassword = "Bahez.151@",
            newPassword = "Bahez.151",
            emailError = null
        ))
        dispatcher.scheduler.advanceUntilIdle()
        assertThat(emailAndPasswordChangeViewModel.emailAndPasswordChangeUiState.value).isEqualTo(expectedData)
    }

    @Test
    fun`if confirm email not equal to oldEmail should return error message` () = runTest {
        val expectedData = EmailAndPasswordChangeUiState(
           confirmEmailError = "Emails do not match.",
            currentEmailError = "Invalid input",
            emailError = null
        )
        emailAndPasswordChangeViewModel.updateEmail(emailAndPasswordChangeUiState = EmailAndPasswordChangeUiState(
            newEmail = "bahehamid04@gmail.com",
            oldEmail = "bahezhamid@gmail.com",
            confirmEmail = "bahezhamid00@gmail.com"
        ),
            password = "Bahez.151@"
        )

        dispatcher.scheduler.advanceUntilIdle()
        assertThat(emailAndPasswordChangeViewModel.emailAndPasswordChangeUiState.value).isEqualTo(expectedData)
    }

    @Test
    fun`if confirm password not equal to oldPassword should return error message` () = runTest {
        val expectedData = EmailAndPasswordChangeUiState(
            confirmPasswordError = "Passwords do not match.",
            passwordError = null,
            currentPasswordError = "Invalid input"
        )
        emailAndPasswordChangeViewModel.updatePassword(emailAndPasswordChangeUiState = EmailAndPasswordChangeUiState(
            password = "Bahez.151@1",
            confirmPassword = "Bahez.151@",
            newPassword = "Bahez.151@12"
        ))
        dispatcher.scheduler.advanceUntilIdle()
        assertThat(emailAndPasswordChangeViewModel.emailAndPasswordChangeUiState.value).isEqualTo(expectedData)
    }

}