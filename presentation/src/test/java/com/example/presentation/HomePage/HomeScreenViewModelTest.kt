package com.example.presentation.HomePage

import com.example.domain.entity.AnimeDataWithPage
import com.example.domain.repository.AnimeDataRepository
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
class HomeScreenViewModelTest {
    private lateinit var homeScreenViewModel: HomeScreenViewModel
    private val animeDataRepository : AnimeDataRepository = mockk()
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        homeScreenViewModel = HomeScreenViewModel(
            animeDataRepository = animeDataRepository
        )
    }

    @After
    fun tearsDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getAnimeData Must Get Anime Data`() {
        val expectedData = AnimeDataWithPage(page = 1)
        coEvery {
            animeDataRepository.getAnimeByScore(9)
        } returns expectedData

        homeScreenViewModel.getAnimeData()

        dispatcher.scheduler.advanceUntilIdle()
        assertThat(homeScreenViewModel.uiState.value).isEqualTo(expectedData)
        coVerify {
            animeDataRepository.getAnimeByScore(9)
        }
    }
}