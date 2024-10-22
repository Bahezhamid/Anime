package com.example.presentation.AllAnimePage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import com.example.domain.entity.AllGenreData
import com.example.domain.entity.AnimeDataWithPage
import com.example.domain.repository.AnimeDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

sealed interface AllGenreUiState {
    data class Success(val genres: List<AllGenreData?>?) : AllGenreUiState
    object Error : AllGenreUiState
    object Loading : AllGenreUiState
}
sealed interface AllAnimeByGenreUiState {
    data class Success( val animeList: AnimeDataWithPage?) : AllAnimeByGenreUiState
    object Error : AllAnimeByGenreUiState
    object Loading : AllAnimeByGenreUiState
}

@HiltViewModel
class AllAnimeScreenViewModel @Inject constructor(private val animeDataRepository: AnimeDataRepository) : ViewModel() {
    private var _allGenresUiState = MutableStateFlow<AllGenreUiState>(AllGenreUiState.Loading)
    val allGenresUiState get() = _allGenresUiState.asStateFlow()

    private var _allSelectedAnimeByGenre = MutableStateFlow<AllAnimeByGenreUiState>(AllAnimeByGenreUiState.Loading)
    val allSelectedAnimeByGenre = _allSelectedAnimeByGenre.asStateFlow()

    private var allAnimeData: AnimeDataWithPage? = null

    private var _selectedGenre = MutableStateFlow(0)
    val selectedGenre = _selectedGenre
    private val _currentPage = MutableStateFlow(1)
    val currentPage = _currentPage
    fun updateSelectedGenre(selectedGenreId : Int) {
        _selectedGenre.value = selectedGenreId
    }
    fun updateCurrentPage(currentPage  : Int) {
        _currentPage.value = currentPage
    }
    init {
        fetchAllData()
        getAnimeByGenre()
    }

    fun fetchAllData() {
        viewModelScope.launch {
            try {
                _allGenresUiState.value = AllGenreUiState.Loading
                val genres = animeDataRepository.getAllGenres()
                _allGenresUiState.value = AllGenreUiState.Success(genres)
            } catch (e: IOException) {
                _allGenresUiState.value = AllGenreUiState.Error
            } catch (e: HttpException) {
                _allGenresUiState.value = AllGenreUiState.Error
            } catch (e: Exception) {
                _allGenresUiState.value = AllGenreUiState.Error
            }
        }
    }

    fun getAnimeByGenre() {
        viewModelScope.launch {
            _allSelectedAnimeByGenre.value = AllAnimeByGenreUiState.Loading

            _allSelectedAnimeByGenre.value = try {
                val filteredAnime = animeDataRepository.getAnimeByGenre(genreId = _selectedGenre.value , page = _currentPage.value)
                allAnimeData = filteredAnime
                AllAnimeByGenreUiState.Success(allAnimeData)
            } catch (e: IOException) {
                AllAnimeByGenreUiState.Error
            } catch (e: HttpException) {
                AllAnimeByGenreUiState.Error
            } catch (e: Exception) {
                AllAnimeByGenreUiState.Error
            }
        }
    }
}
