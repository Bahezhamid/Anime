package com.example.presentation.UserDetailsPage

import androidx.lifecycle.ViewModel
import com.example.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    val _favoriteCount = MutableStateFlow(0)
    val favoriteCount = _favoriteCount.asStateFlow()
    suspend fun getFavoriteCount(userId: String) {
        _favoriteCount.value =  try {
            authRepository.getNumberOfAddedAnimeToFavorite(userId = userId)
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }
}
