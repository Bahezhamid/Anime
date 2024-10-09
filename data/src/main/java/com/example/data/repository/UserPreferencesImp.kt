package com.example.data.repository

import com.example.data.network.UserPreferencesService
import com.example.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow

class UserPreferencesImp (
   private val userPreferencesService: UserPreferencesService
) : UserPreferencesRepository{
    override suspend fun storeUserPreferences(email: String, password: String)
    = userPreferencesService.saveUserCredentials(email = email, password = password)
    override suspend fun getUserEmail(): Flow<String?> = userPreferencesService.userEmail
    override suspend fun getUserPassword(): Flow<String?> = userPreferencesService.userPassword
}