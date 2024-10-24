package com.example.presentation.SearchScreenPage

import com.example.domain.entity.AnimeDataWithPage
import com.example.domain.repository.AnimeDataRepository
import com.example.domain.repository.AuthRepository
import com.example.presentation.SavedAnimePage.SavedAnimeViewModel
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
class SearchScreenViewModelTest {
    private lateinit var searchScreenViewModel: SearchScreenViewModel
    private val animeDataRepository : AnimeDataRepository = mockk()

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        searchScreenViewModel = SearchScreenViewModel(
            animeDataRepository = animeDataRepository,
        )
    }
    @After
    fun tearsDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `updateSearchTextFieldValue fun should update text field value `() {
        val expectedValue = "Attack"

        searchScreenViewModel.updateSearchTextFieldValue(newValue = expectedValue)

        assertThat(searchScreenViewModel.searchTextFieldValue.value).isEqualTo(expectedValue)

    }

    @Test
    fun `getSearchedAnime fun should return list of anime by given name `() = runTest{
        val expectedData = AnimeDataWithPage(
            page = 1,
            animeData = emptyList()
        )
        coEvery { animeDataRepository.getAnimeByName(animeName = "attack") } returns expectedData
        searchScreenViewModel.getSearchedAnime(animeName = "attack")
        dispatcher.scheduler.advanceUntilIdle()

        assertThat(searchScreenViewModel.searchedAnimeUiState.value).isEqualTo(SearchedAnimeUiState.Success(expectedData))
        coVerify {  animeDataRepository.getAnimeByName(animeName = "attack") }
    }
}