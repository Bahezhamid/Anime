package com.example.presentation.AnimeDetailsPage

import com.example.domain.entity.AnimeAllCharactersData
import com.example.domain.entity.AnimeDataById
import com.example.domain.repository.AnimeDataRepository
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
import com.google.common.truth.Truth.assertThat

@ExperimentalCoroutinesApi
class AnimeDetailsViewModelTest{
    private lateinit var animeDetailsViewModel: AnimeDetailsViewModel
    private val animeDataRepository : AnimeDataRepository = mockk(relaxed = true)
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        animeDetailsViewModel = AnimeDetailsViewModel(
            animeDataRepository = animeDataRepository
        )
    }

    @After
    fun tearsDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun ` getAnimeByd fun should return AnimeData by give Id ` () =  runTest{
        val animeDetails = AnimeDataById(
            id = 1,
            title = "aa",
            imageUrl = "aa",
            trailerImg = "aa",
            trailerUrl = "aa",
            releaseDate = "aa",
            popularityRank = 1,
            globalRank = 1,
            genre = emptyList(),
            episodes = 1,
            episodesMin = "aa",
            description = "aa",
            animeUrl = "a"
        )
        val characterOne = AnimeAllCharactersData(
            id = 1,
            imageUrl = "aa"
        )
        val characterTwo = AnimeAllCharactersData(
            id = 2,
            imageUrl = "bb"
        )
        val listOfCharacters = listOf(characterOne, characterTwo)
        coEvery { animeDataRepository.getAnimeDataById(1) } returns animeDetails
        coEvery { animeDataRepository.getAllCharacters(1) } returns listOfCharacters

        animeDetailsViewModel.getAnimeDataById(id = 1)
        dispatcher.scheduler.advanceUntilIdle()

        assertThat(animeDetailsViewModel.animeDataByIdUiState.value)
            .isEqualTo(AnimeDetailsUiState.Success(animeDetails = animeDetails , animeCharacters = listOfCharacters))

        coVerify { animeDataRepository.getAnimeDataById(1) }
        coVerify { animeDataRepository.getAllCharacters(id = 1) }
    }
}