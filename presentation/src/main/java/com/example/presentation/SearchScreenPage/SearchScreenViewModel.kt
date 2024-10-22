package com.example.presentation.SearchScreenPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import com.example.domain.entity.AnimeDataWithPage
import com.example.domain.repository.AnimeDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

sealed interface SearchedAnimeUiState {
    data class Success( val SearchedAnime: AnimeDataWithPage?) : SearchedAnimeUiState
    object Error : SearchedAnimeUiState
    object Loading : SearchedAnimeUiState
}

@HiltViewModel
class SearchScreenViewModel @Inject constructor( private val animeDataRepository: AnimeDataRepository) : ViewModel() {
    private val _searchTextFieldValue = MutableStateFlow("")
    val searchTextFieldValue: StateFlow<String> get() = _searchTextFieldValue.asStateFlow()
    private var _searchedAnimeUiState = MutableStateFlow<SearchedAnimeUiState>(
        SearchedAnimeUiState.Success(null))
    val searchedAnimeUiState = _searchedAnimeUiState.asStateFlow()
    fun updateSearchTextFieldValue(newValue : String) {
        _searchTextFieldValue.value = newValue
    }

    fun getSearchedAnime(animeName : String) {
        viewModelScope.launch {
            try {
                _searchedAnimeUiState.value = SearchedAnimeUiState.Loading
                val searchedAnime = animeDataRepository.getAnimeByName(animeName = animeName)
                _searchedAnimeUiState.value = SearchedAnimeUiState.Success(searchedAnime)
            } catch (e: IOException) {
                _searchedAnimeUiState.value = SearchedAnimeUiState.Error
            } catch (e: HttpException) {
                _searchedAnimeUiState.value = SearchedAnimeUiState.Error
            } catch (e: Exception) {
                _searchedAnimeUiState.value = SearchedAnimeUiState.Error
            }
        }
    }
}