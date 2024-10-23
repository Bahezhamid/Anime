package com.example.presentation.CharacterDetailsPage

import com.example.domain.entity.AnimeCharactersData
import com.example.domain.repository.AnimeDataRepository
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
class CharacterDetailsViewModelTest {
    private lateinit var characterDetailsViewModel: CharacterDetailsViewModel
    private val animeDataRepository : AnimeDataRepository  = mockk(relaxed = true)
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        characterDetailsViewModel = CharacterDetailsViewModel(
            animeDataRepository = animeDataRepository
        )
    }
    @After
    fun tearsDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getCharacterDetail fun should return characters all details ` () = runTest {
        val charactersAllDetails = AnimeCharactersData(
            id = 1,
            name = "aa",
            description = "aa",
            imageUrl = "aa",
            favorites = 1,
            listOfAnime = emptyList(),
            listOfVoiceActor = emptyList()
        )

        coEvery { animeDataRepository.getAllCharactersData(id = 1) } returns charactersAllDetails
        characterDetailsViewModel.getCharactersDetail(id = 1)
        dispatcher.scheduler.advanceUntilIdle()

        assertThat(characterDetailsViewModel.charactersDetails.value).isEqualTo(CharactersDetailsUiState.Success(charactersAllDetails))
        coVerify { animeDataRepository.getAllCharactersData(id = 1) }
    }
}