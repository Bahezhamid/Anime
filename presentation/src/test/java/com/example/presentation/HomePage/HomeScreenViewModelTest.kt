package com.example.presentation.HomePage

import com.example.domain.entity.AnimeDataWithPage
import com.example.domain.entity.FavoriteAnime
import com.example.domain.repository.AnimeDataRepository
import com.example.domain.repository.AuthRepository
import com.example.presentation.LoginAndSignUpPage.UsersUiState
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
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
class HomeScreenViewModelTest {
    private lateinit var homeScreenViewModel: HomeScreenViewModel
    private val animeDataRepository : AnimeDataRepository = mockk(relaxed = true)
    private val authRepository : AuthRepository = mockk()
    private val dispatcher = StandardTestDispatcher()
    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        homeScreenViewModel = HomeScreenViewModel(
            animeDataRepository = animeDataRepository,
            authRepository = authRepository
        )
    }

    @After
    fun tearsDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getAnimeData Must Get Anime Data`() {
        val expectedData = AnimeDataUiState.Success(AnimeDataWithPage(page = 1))
        coEvery {
            animeDataRepository.getAnimeByScore(9)
        } returns expectedData.animeData

        homeScreenViewModel.getAnimeData()

        dispatcher.scheduler.advanceUntilIdle()
        assertThat(homeScreenViewModel.uiState.value).isEqualTo(expectedData)
        coVerify {
            animeDataRepository.getAnimeByScore(9)
        }
    }

    @Test
    fun ` updateUserUiState fun should change loginUiState value `() {
        val expectedDate = UsersUiState(
            email = "BahezHamid00@gmail.com",
            userName = "Bahez",
            userid = "123"
        )

        homeScreenViewModel.updateUserUiState(usersUiState = expectedDate)

        assertThat(homeScreenViewModel.loginUiState.value).isEqualTo(expectedDate)
    }

    @Test
    fun `if Anime not added to favorite  updateFavoriteUiState fun Should return false `() = runTest{

        coEvery { authRepository.getAnimeStatus(animeId = 1, userId = "123") } returns false

        homeScreenViewModel.updateFavoriteStatus(animeId = 1, userId = "123")
        dispatcher.scheduler.advanceUntilIdle()

        assertThat(homeScreenViewModel.isAnimeAddedToFavorite.value).isEqualTo(false)
        coVerify { authRepository.getAnimeStatus(animeId = 1 , userId = "123")}
    }

    @Test
    fun `if Anime  added to favorite  updateFavoriteUiState fun Should return true `() = runTest{

        coEvery { authRepository.getAnimeStatus(animeId = 1, userId = "123") } returns true

        homeScreenViewModel.updateFavoriteStatus(animeId = 1, userId = "123")
        dispatcher.scheduler.advanceUntilIdle()

        println(homeScreenViewModel.isAnimeAddedToFavorite.value)

        assertThat(homeScreenViewModel.isAnimeAddedToFavorite.value).isEqualTo(true)
        coVerify { authRepository.getAnimeStatus(animeId = 1 , userId = "123")}
    }

    @Test
    fun `insertAnimeToFavorite must insert the anime to db and make isAnimeAddedToFavorite To true` () = runTest {
        val favoriteAnime = FavoriteAnime(
            animeId = 1,
            animeName = "attack",
            animePoster = "attackPoster",
            userId = "123"
        )
        coEvery { authRepository.addAnimeToFavorite(favoriteAnime) } just runs

        homeScreenViewModel.insertAnimeToFavorite(favoriteAnime)
        dispatcher.scheduler.advanceUntilIdle()
        assertThat(homeScreenViewModel.isAnimeAddedToFavorite.value).isTrue()
        coVerify { authRepository.addAnimeToFavorite(favoriteAnime) }
    }

    @Test
    fun `deleteAnimeFromFavorite must delete the anime from db and make isAnimeAddedToFavorite To false` () = runTest {
       val userId = "123"
        val animeId = 1
        coEvery { authRepository.deleteAnimeFromFavorite(userId = userId, animeId = animeId) } just runs

        homeScreenViewModel.deleteAnimeFromFavorite(animeId = animeId , userId = userId)
        dispatcher.scheduler.advanceUntilIdle()
        assertThat(homeScreenViewModel.isAnimeAddedToFavorite.value).isFalse()
        coVerify { authRepository.deleteAnimeFromFavorite(animeId = animeId , userId = userId) }
    }


}