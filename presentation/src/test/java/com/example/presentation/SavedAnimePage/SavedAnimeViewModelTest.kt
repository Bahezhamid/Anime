package com.example.presentation.SavedAnimePage

import com.example.domain.entity.FavoriteAnime
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.UserPreferencesRepository
import com.example.presentation.LoginAndSignUpPage.LoginAndSignUpViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

@ExperimentalCoroutinesApi
class SavedAnimeViewModelTest {
    private lateinit var savedAnimeViewModel: SavedAnimeViewModel
    private val authRepository : AuthRepository = mockk()

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        savedAnimeViewModel = SavedAnimeViewModel(
            authRepository = authRepository,
        )
    }
    @After
    fun tearsDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `removeSaveAnime fun should remove anime data` () {
        val expectedData = SavedAnimeUiState.Success(null)
        savedAnimeViewModel.removeSavedAnime()
        assertThat(savedAnimeViewModel.savedAnimeUiState.value).isEqualTo(expectedData)
    }

    @Test
    fun `getAllSavedAnime fun should return all saved anime for that user `() {
        val expectedData  : List<FavoriteAnime> = emptyList()
        coEvery { authRepository.getAllSavedAnime(userId = "123") } returns expectedData

        savedAnimeViewModel.getAllSavedAnime(userId = "123")
        dispatcher.scheduler.advanceUntilIdle()

        assertThat(savedAnimeViewModel.savedAnimeUiState.value).isEqualTo(SavedAnimeUiState.Success(expectedData))
        coVerify { authRepository.getAllSavedAnime(userId = "123") }
    }

}