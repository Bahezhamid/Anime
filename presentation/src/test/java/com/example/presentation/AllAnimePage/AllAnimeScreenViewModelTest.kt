package com.example.presentation.AllAnimePage

import com.example.domain.entity.AllGenreData
import com.example.domain.entity.AnimeDataWithPage
import com.example.domain.repository.AnimeDataRepository
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
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest

@ExperimentalCoroutinesApi
class AllAnimeScreenViewModelTest {
    private lateinit var allAnimeScreenViewModel: AllAnimeScreenViewModel
    private val animeDataRepository : AnimeDataRepository = mockk(relaxed = true)
    private val dispatcher  = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        allAnimeScreenViewModel = AllAnimeScreenViewModel(
            animeDataRepository = animeDataRepository
        )
    }

    @After
    fun tearsDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun ` updateSelectedGenre fun should update selected genre uiValue to selected one` () {
        allAnimeScreenViewModel.updateSelectedGenre(3)
        assertThat(allAnimeScreenViewModel.selectedGenre.value).isEqualTo(3)
    }

    @Test
    fun ` updateCurrentPage fun should update current Page uiValue to selected one` () {
        allAnimeScreenViewModel.updateCurrentPage(3)
        assertThat(allAnimeScreenViewModel.currentPage.value).isEqualTo(3)
    }


    @Test
    fun `fetchAllData fun should get a list of genre ` ()=   runTest{
        val firstGenre = AllGenreData(
            id = 1,
            title = "action",
            url = "122"
        )
        val secondGenre = AllGenreData(
            id = 2,
            title = "comedy",
            url = "1223"
        )
        val listOfGenre = listOf(firstGenre,secondGenre)
        coEvery { animeDataRepository.getAllGenres() }returns listOfGenre
        allAnimeScreenViewModel.fetchAllData()

        dispatcher.scheduler.advanceUntilIdle()
        assertThat(allAnimeScreenViewModel.allGenresUiState.value).isEqualTo(AllGenreUiState.Success(listOfGenre))
        coVerify { animeDataRepository.getAllGenres() }
    }

    @Test
    fun ` get AnimeByGenre fun should return list of anime by selected genre `() = runTest {
        val expectedData = AnimeDataWithPage(page = 1 , animeData = emptyList())
        coEvery { animeDataRepository.getAnimeByGenre(genreId = 0 , page = 1) } returns expectedData

        allAnimeScreenViewModel.getAnimeByGenre()

        dispatcher.scheduler.advanceUntilIdle()

        assertThat(allAnimeScreenViewModel.allSelectedAnimeByGenre.value).isEqualTo(AllAnimeByGenreUiState.Success(expectedData))
        coVerify { animeDataRepository.getAnimeByGenre(genreId = 0 , page = 1) }
    }
}