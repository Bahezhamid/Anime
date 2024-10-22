package com.example.presentation.LoginAndSignUpPage

import com.example.domain.entity.UsersData
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.UserPreferencesRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

@ExperimentalCoroutinesApi
class LoginAndSignUpViewModelTest{
    private lateinit var loginAndSignUpViewModel: LoginAndSignUpViewModel
    private val authRepository : AuthRepository  = mockk()
    private val userPreferencesRepository : UserPreferencesRepository = mockk(relaxed = true)
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        loginAndSignUpViewModel = LoginAndSignUpViewModel(
            authRepository = authRepository,
            userPreferencesRepository = userPreferencesRepository
        )
    }
    @After
    fun tearsDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `empty Email Field should return error message` () = runTest {
        val expectedData = LoginAndSignUpUiState(
            emailError = "Invalid email format.",
        )
        loginAndSignUpViewModel.saveAccount(signUpState =
        LoginAndSignUpUiState(email = "" , password = "Bahez.151@" , userName = "Bahez", confirmPassword = "Bahez.151@" ))
        dispatcher.scheduler.advanceUntilIdle()
        assertThat(loginAndSignUpViewModel.uiState.value).isEqualTo(expectedData)
    }

    @Test
    fun `empty Password Field Should return Error Message` () = runTest {
        val expectedData = LoginAndSignUpUiState(
            passwordError = "Password must be at least 8 characters, include an upper case letter, a number, and a special character.",
            confirmPasswordError = "Passwords do not match."
        )
        loginAndSignUpViewModel.saveAccount(signUpState =
        LoginAndSignUpUiState(email = "bahezhamid00@gmail.com", password = "" , userName = "Bahez" , confirmPassword = "Bahez.151@"))
        dispatcher.scheduler.advanceUntilIdle()
        assertThat(loginAndSignUpViewModel.uiState.value).isEqualTo(expectedData)
    }
    @Test
    fun `empty UserName Field Should return Error Message` () = runTest {
        val expectedData = LoginAndSignUpUiState(
         userNameError = "Please Enter Your Username."
        )
        loginAndSignUpViewModel.saveAccount(signUpState =
        LoginAndSignUpUiState(email = "bahezhamid00@gmail.com", password = "Bahez.151@" , userName = "" , confirmPassword = "Bahez.151@"))
        dispatcher.scheduler.advanceUntilIdle()
        assertThat(loginAndSignUpViewModel.uiState.value).isEqualTo(expectedData)
    }
    @Test
    fun `if confirm password not equal to password should return error message` () = runTest {
        val expectedData = LoginAndSignUpUiState(
            confirmPasswordError = "Passwords do not match."
        )
        loginAndSignUpViewModel.saveAccount(signUpState =
        LoginAndSignUpUiState(email = "bahezhamid00@gmail.com", password = "Bahez.151@" , userName = "Bahez" , confirmPassword = "Bahez.151"))
        dispatcher.scheduler.advanceUntilIdle()
        assertThat(loginAndSignUpViewModel.uiState.value).isEqualTo(expectedData)
    }
    @Test
    fun `if email or password is wrong should return error message` () = runTest {
        val expectedData = UsersData(
            errorMessage = "Wrong Email Or Password",
            isSuccess = false
        )
        coEvery {
            authRepository.login(email = "bahezhamid00@gmail.com", password = "Bahez.151@")
        } returns expectedData

        val expectedUserUiState = UsersUiState(
            errorMessage = expectedData.errorMessage,
            isSuccess = expectedData.isSuccess,
            isLoading = false
        )
        loginAndSignUpViewModel.login(email = "bahezhamid00@gmail.com" , password = "Bahez.151@")
        dispatcher.scheduler.advanceUntilIdle()
        assertThat(loginAndSignUpViewModel.loginUiState.value).isEqualTo(expectedUserUiState)
        coVerify {
            authRepository.login(email = "bahezhamid00@gmail.com", password = "Bahez.151@")
        }
    }

    @Test
    fun `if email pr password is exist should return isSuccess` () = runTest {
        val expectedData = UsersData(
            email = "bahezhamid@gmail.com",
            userName = "Bahez",
            userId = "123",
            errorMessage = "",
            isSuccess = true
        )
        coEvery {
            authRepository.login(email = "bahezhamid@gmail.com", password = "Bahez.151@")
        } returns expectedData
        val expectedUserUiState = UsersUiState(
            isSuccess = expectedData.isSuccess,
            email = expectedData.email,
            userName = expectedData.userName,
            userid = expectedData.userId,
            isLoading = false
        )
        loginAndSignUpViewModel.login(email = "bahezhamid@gmail.com" , password = "Bahez.151@")
        dispatcher.scheduler.advanceUntilIdle()
        assertThat(loginAndSignUpViewModel.loginUiState.value).isEqualTo(expectedUserUiState)
        coVerify {
            authRepository.login(email = "bahezhamid@gmail.com", password = "Bahez.151@")
        }
    }
    @Test
    fun `if Remember Me is on should store username and password in userPreferences`() = runTest {
        val expectedEmail = "bahezhamid00@gmial.com"
        val expectedPassword = "Bahez.151@"
        val expectedData = UsersData(
            email = "bahezhamid00@gmail.com",
            userName = "Bahez",
            userId = "123",
            errorMessage = "",
            isSuccess = true
        )
        coEvery {
            authRepository.login(email = expectedEmail, password = expectedPassword)
        } returns expectedData
        coEvery {
            userPreferencesRepository.storeUserPreferences(email = expectedEmail, password = expectedPassword)
        } just runs
        loginAndSignUpViewModel.login(email = expectedEmail , password = expectedPassword)
        dispatcher.scheduler.advanceUntilIdle()
        coVerify(exactly = 1) {
            userPreferencesRepository.storeUserPreferences(expectedEmail, expectedPassword)
        }
    }

    @Test
    fun `wrong email address input for forget password should return error message`() = runTest {
        val expectedData = UsersUiState(
            isPasswordResetSent = false,
            isLoading = false,
            isSuccess = false,
            errorMessage = "Wrong Email Address"
        )
        coEvery {
            authRepository.forgetPassword(email = "bahezhamid00@gmail.com")
        } returns false
        loginAndSignUpViewModel.forgetPassword(signUpState = LoginAndSignUpUiState(email = "bahezhamid00@gmail.com"))
        assertThat(loginAndSignUpViewModel.loginUiState.value).isEqualTo(expectedData)
    }

    @Test
    fun ` Invalid email format for forget password should return error message ` () = runTest {
        val expectedData = LoginAndSignUpUiState(emailError = "Invalid email format.")
        loginAndSignUpViewModel.forgetPassword(signUpState = LoginAndSignUpUiState(email = "bahez"))

        assertThat(loginAndSignUpViewModel.uiState.value).isEqualTo(expectedData)
    }


    @Test
    fun `SignOut fun should empty the uiState`() =  runTest{
        val expectedUiState = LoginAndSignUpUiState(
            email = "",
            password = "",
            isRememberMeOn = false,
            confirmPassword = "",
            userName = "",
        )
        val expectedUserUiState = UsersUiState()
        coEvery {
            authRepository.signUp(email ="bahezhamid00@gmail.com" , password = "Bahez.151@" , userName = "Bahez" )
        } returns UsersData(
            email = "bahezhamid00@gmail.com",
            userName = "Bahez",
            userId = "123",
            errorMessage = "",
            isSuccess = true
        )

        coEvery {
            authRepository.signOut()
        } just runs

        coEvery {
            userPreferencesRepository.storeUserPreferences("", "")
        } just runs
        loginAndSignUpViewModel.saveAccount(signUpState = LoginAndSignUpUiState(
            email = "bahezhamid00@gmail.com",
            password = "Bahez.151@",
            confirmPassword = "Bahez.151@",
            userName = "Bahez"
        ))
        loginAndSignUpViewModel.signOut()

        dispatcher.scheduler.advanceUntilIdle()
        assertThat(loginAndSignUpViewModel.uiState.value).isEqualTo(expectedUiState)
        assertThat(loginAndSignUpViewModel.loginUiState.value).isEqualTo(expectedUserUiState)
    }
}