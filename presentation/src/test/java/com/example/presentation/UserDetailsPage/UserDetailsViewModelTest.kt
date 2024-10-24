package com.example.presentation.UserDetailsPage

import com.example.domain.repository.AnimeDataRepository
import com.example.domain.repository.AuthRepository
import com.example.presentation.SearchScreenPage.SearchScreenViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
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
class UserDetailsViewModelTest {
    private lateinit var userDetailsViewModel: UserDetailsViewModel
    private val authRepository :AuthRepository = mockk()
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        userDetailsViewModel = UserDetailsViewModel(
            authRepository = authRepository,
        )
    }
    @After
    fun tearsDown() {
        Dispatchers.resetMain()
    }
    @Test
    fun `getFavoriteCount must return number anime added to favorite for given user` () = runTest {
        val expectedData = 9
        coEvery { authRepository.getNumberOfAddedAnimeToFavorite(userId = "123") } returns expectedData

        userDetailsViewModel.getFavoriteCount(userId = "123")
        dispatcher.scheduler.advanceUntilIdle()

        assertThat(userDetailsViewModel.favoriteCount.value).isEqualTo(expectedData)
    }
}