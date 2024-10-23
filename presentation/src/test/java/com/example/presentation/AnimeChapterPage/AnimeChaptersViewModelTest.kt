package com.example.presentation.AnimeChapterPage

import com.example.domain.entity.AllAnimeChaptersData
import com.example.domain.repository.AnimeDataRepository
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
class AnimeChaptersViewModelTest {
    private lateinit var animeChaptersViewModel : AnimeChaptersViewModel
    private val animeDataRepository : AnimeDataRepository = mockk(relaxed = true)
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        animeChaptersViewModel = AnimeChaptersViewModel(
            animeDataRepository = animeDataRepository
        )
    }

    @After
    fun tearsDown() {
        Dispatchers.resetMain()
    }
    @Test
    fun ` updateCurrentPage fun should update current Page uiValue to selected one` () {
        animeChaptersViewModel.updateCurrentPage(3)
        assertThat(animeChaptersViewModel.currentPage.value).isEqualTo(3)
    }

    @Test
    fun `getAllChapters fun should return all chapters if id and page is correct` () = runTest {
        val id = 1
        val page = 3
        val allAnimeChapters = AllAnimeChaptersData(
            page = 3,
            listOfChapters = emptyList()
        )
        coEvery { animeDataRepository.getAnimeChapters(id = id , page = page) } returns allAnimeChapters

        animeChaptersViewModel.getAllChapters(id = id , page = page)
        dispatcher.scheduler.advanceUntilIdle()

        assertThat(animeChaptersViewModel.animeChaptersUiState.value).isEqualTo(AnimeChaptersUiState.Success(allAnimeChapters))
    }

}