package com.example.presentation.SavedAnimePage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.FavoriteAnime
import com.example.domain.repository.AnimeDataRepository
import com.example.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

sealed interface SavedAnimeUiState {
    data class Success(val savedAnimeData: List<FavoriteAnime>?) : SavedAnimeUiState
    object Error : SavedAnimeUiState
    object Loading : SavedAnimeUiState
}

@HiltViewModel
class SavedAnimeViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _savedAnimeUiState = MutableStateFlow<SavedAnimeUiState>(SavedAnimeUiState.Loading)
    val savedAnimeUiState = _savedAnimeUiState

    fun removeSavedAnime() {
        _savedAnimeUiState.value = SavedAnimeUiState.Success(null)
    }
    fun getAllSavedAnime(userId: String) {
        viewModelScope.launch {
            _savedAnimeUiState.value = SavedAnimeUiState.Loading

            try {
                val favorites = authRepository.getAllSavedAnime(userId = userId)
                _savedAnimeUiState.value = SavedAnimeUiState.Success(favorites)
            } catch (e: Exception) {
                Log.e("SavedAnimeViewModel", "Error fetching saved anime", e)
                _savedAnimeUiState.value = SavedAnimeUiState.Error
            }
        }

    }
}
