package com.example.presentation.AnimeChapterPage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import com.example.domain.entity.AllAnimeChaptersData
import com.example.domain.entity.AnimeChaptersData
import com.example.domain.repository.AnimeDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

sealed interface AnimeChaptersUiState {
    data class Success(val animeChapters: AllAnimeChaptersData?) : AnimeChaptersUiState
    object Error : AnimeChaptersUiState
    object Loading : AnimeChaptersUiState
}

@HiltViewModel
class AnimeChaptersViewModel @Inject constructor(private val animeDataRepository: AnimeDataRepository) : ViewModel() {

    private var _animeChaptersUiState = MutableStateFlow<AnimeChaptersUiState>(AnimeChaptersUiState.Loading)
    val animeChaptersUiState get() = _animeChaptersUiState.asStateFlow()
    private val _currentPage = MutableStateFlow(1)
    val currentPage = _currentPage
    fun updateCurrentPage(currentPage  : Int) {
        _currentPage.value = currentPage
    }
    fun getAllChapters(id  :Int, page : Int) {
        viewModelScope.launch {
            _animeChaptersUiState.value = AnimeChaptersUiState.Loading
            _animeChaptersUiState.value = try {
                val result = animeDataRepository.getAnimeChapters(id=id,page=page)
                AnimeChaptersUiState.Success(result)

            } catch (e: IOException) {
                Log.e("AnimeChapter", "IOException while fetching anime data: ${e.message}")
                AnimeChaptersUiState.Error
            } catch (e: HttpException) {
                Log.e("AnimeChapter", "HttpException while fetching anime data: ${e.message}")
                AnimeChaptersUiState.Error
            }
        }
    }
}