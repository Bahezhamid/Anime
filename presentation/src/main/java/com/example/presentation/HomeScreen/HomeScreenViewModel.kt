package com.example.presentation.HomeScreen

import android.net.http.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.AnimeDataWithPage
import com.example.domain.repository.AnimeDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val animeDataRepository: AnimeDataRepository
) : ViewModel() {
    private var _uiState = MutableStateFlow<AnimeDataWithPage?>(null)
    val uiState get() = _uiState.asStateFlow()
    init {
        getAnimeData()
    }

    fun getAnimeData() {
        viewModelScope.launch {
            try {
                val result = animeDataRepository.getAnimeByScore(minScore = 9)
                _uiState.value = result

            } catch (e: IOException) {
                Log.d("errrr","error")
            }
        }
    }
}