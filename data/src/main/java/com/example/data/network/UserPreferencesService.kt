package com.example.data.network

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class UserPreferencesService @Inject constructor(private val dataStore: DataStore<Preferences>) {

    private companion object {
        val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        val USER_PASSWORD_KEY = stringPreferencesKey("user_password")
        const val TAG = "UserPreferencesRepo"
    }

    suspend fun saveUserCredentials(email: String, password: String) {
        dataStore.edit { preferences ->
            preferences[USER_EMAIL_KEY] = email
            preferences[USER_PASSWORD_KEY] = password
        }
    }

    val userEmail: Flow<String?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[USER_EMAIL_KEY]
        }

    val userPassword: Flow<String?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[USER_PASSWORD_KEY]
        }
}