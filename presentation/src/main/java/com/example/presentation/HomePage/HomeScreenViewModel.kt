package com.example.presentation.HomePage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import com.example.domain.entity.AnimeDataWithPage
import com.example.domain.entity.FavoriteAnime
import com.example.domain.repository.AnimeDataRepository
import com.example.domain.repository.AuthRepository
import com.example.presentation.LoginAndSignUpPage.UsersUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

sealed interface AnimeDataUiState {
    data class Success(val animeData: AnimeDataWithPage?) : AnimeDataUiState
    object Error : AnimeDataUiState
    object Loading : AnimeDataUiState
}

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val animeDataRepository: AnimeDataRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginUiState = MutableStateFlow(UsersUiState())
    val loginUiState: StateFlow<UsersUiState> get() = _loginUiState.asStateFlow()
    private var _uiState = MutableStateFlow<AnimeDataUiState>(AnimeDataUiState.Loading)
    val uiState get() = _uiState.asStateFlow()
    private var _isAnimeAddedToFavorite = MutableStateFlow<Boolean>(false)
    val isAnimeAddedToFavorite = _isAnimeAddedToFavorite

    init {
        getAnimeData()
    }
    fun updateUserUiState(usersUiState: UsersUiState) {
        _loginUiState.value = usersUiState
    }

    fun updateFavoriteStatus(animeId: Int, userId: String) {
        viewModelScope.launch {
            _isAnimeAddedToFavorite.value = withContext(Dispatchers.IO) {
                authRepository.getAnimeStatus(animeId = animeId, userId = userId)
            }
        }
    }

    fun getAnimeData() {
        viewModelScope.launch {
            _uiState.value = AnimeDataUiState.Loading
            try {
                val result = animeDataRepository.getAnimeByScore(minScore = 9)
                _uiState.value = AnimeDataUiState.Success(result)

                val animeId = (result?.animeData?.firstOrNull()?.id) ?: return@launch
                updateFavoriteStatus(animeId, _loginUiState.value.userid)

            } catch (e: IOException) {
                _uiState.value = AnimeDataUiState.Error
            } catch (e: HttpException) {
                _uiState.value = AnimeDataUiState.Error
            }
        }
    }


    fun insertAnimeToFavorite(favoriteAnime: FavoriteAnime) {
        _isAnimeAddedToFavorite.value = true
        viewModelScope.launch {
            try {
              authRepository.addAnimeToFavorite(favoriteAnime = favoriteAnime)
            } catch (e: Exception) {
                Log.e("Firestore", "Error adding favorite", e)
                _isAnimeAddedToFavorite.value = false
            }
        }
    }

    fun deleteAnimeFromFavorite(animeId: Int, userId: String) {
        _isAnimeAddedToFavorite.value = false
        viewModelScope.launch {
            try {
               authRepository.deleteAnimeFromFavorite(animeId = animeId , userId = userId)
            } catch (e: Exception) {
                Log.e("Firestore", "Error deleting favorite", e)
                _isAnimeAddedToFavorite.value = true
            }
        }
    }
}
