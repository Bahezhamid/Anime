package com.example.presentation.CharacterDetailsPage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import com.example.domain.entity.AnimeCharactersData
import com.example.domain.repository.AnimeDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

sealed interface CharactersDetailsUiState {
    data class Success(val charactersDetails: AnimeCharactersData?) : CharactersDetailsUiState
    object Error : CharactersDetailsUiState
    object Loading : CharactersDetailsUiState
}

@HiltViewModel
class CharacterDetailsViewModel @Inject constructor(private val animeDataRepository: AnimeDataRepository) : ViewModel() {
    private val _characterDetails = MutableStateFlow<CharactersDetailsUiState>(CharactersDetailsUiState.Loading)
    val charactersDetails = _characterDetails.asStateFlow()

    fun getCharactersDetail(id : Int) {
        viewModelScope.launch {
            _characterDetails.value = CharactersDetailsUiState.Loading
            _characterDetails.value = try {
                val result = animeDataRepository.getAllCharactersData(id=id)
                Log.d("charactersData",result.toString())
                CharactersDetailsUiState.Success(result)
            }catch (e: IOException) {
                Log.e("AnimeChapter", "IOException while fetching anime data: ${e.message}")
                CharactersDetailsUiState.Error
            } catch (e: HttpException) {
                Log.e("AnimeChapter", "HttpException while fetching anime data: ${e.message}")
                CharactersDetailsUiState.Error
            }

        }

    }
}