package com.example.presentation.AnimeDetailsPage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import com.example.domain.entity.AnimeAllCharactersData
import com.example.domain.entity.AnimeDataById
import com.example.domain.repository.AnimeDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

sealed interface AnimeDetailsUiState {
    data class Success(val animeDetails : AnimeDataById?, val animeCharacters:List <AnimeAllCharactersData?>?) : AnimeDetailsUiState
    object Error : AnimeDetailsUiState
    object Loading : AnimeDetailsUiState
}

@HiltViewModel
class AnimeDetailsViewModel @Inject constructor(private val animeDataRepository: AnimeDataRepository) : ViewModel() {
    private var _animeDataByIdUiState = MutableStateFlow<AnimeDetailsUiState>(AnimeDetailsUiState.Loading)
    val animeDataByIdUiState get() = _animeDataByIdUiState.asStateFlow()
    fun getAnimeDataById(id : Int) {
        viewModelScope.launch {
            _animeDataByIdUiState.value = AnimeDetailsUiState.Loading
            try {
                val animeDetails = animeDataRepository.getAnimeDataById(id)
                val animeCharacters = animeDataRepository.getAllCharacters(id)

                _animeDataByIdUiState.value = AnimeDetailsUiState.Success(
                    animeDetails = animeDetails,
                    animeCharacters = animeCharacters
                )
            } catch (e: IOException) {
                Log.e("AnimeDetailsViewModel", "IOException while fetching anime data: ${e.message}")
                _animeDataByIdUiState.value = AnimeDetailsUiState.Error
            } catch (e: HttpException) {
                Log.e("AnimeDetailsViewModel", "HttpException while fetching anime data: ${e.message}")
                _animeDataByIdUiState.value = AnimeDetailsUiState.Error
            }
        }


    }

}