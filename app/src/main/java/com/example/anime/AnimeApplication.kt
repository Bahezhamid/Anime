package com.example.anime

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
private const val REMEMBER_ME_PREFERENCE_NAME = "remember_me_preference"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = REMEMBER_ME_PREFERENCE_NAME)
@HiltAndroidApp
class AnimeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(applicationContext)
    }
}