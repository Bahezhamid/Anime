package com.example.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    suspend fun storeUserPreferences(email : String, password : String)
    suspend fun getUserEmail() : Flow<String?>
    suspend fun getUserPassword() : Flow<String?>
}