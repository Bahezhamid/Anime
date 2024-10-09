package com.example.anime.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.anime.dataStore
import com.example.data.network.AnimeApiService
import com.example.data.network.FirebaseService
import com.example.data.network.UserPreferencesService
import com.example.data.repository.AuthRepositoryImp
import com.example.data.repository.NetworkAnimeDataRepository
import com.example.data.repository.UserPreferencesImp
import com.example.domain.repository.AnimeDataRepository
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.UserPreferencesRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAndroidApi() : AnimeApiService {
        val baseUrl = "https://api.jikan.moe/v4/"
         val client = OkHttpClient.Builder()
            .connectTimeout(40, TimeUnit.SECONDS)
            .writeTimeout(40, TimeUnit.SECONDS)
            .readTimeout(40, TimeUnit.SECONDS)
            .build()
         val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return  retrofit.create(AnimeApiService::class.java)
    }
    @Provides
    @Singleton
    fun provideAnimeDataRepository(apiService: AnimeApiService) : AnimeDataRepository {
        return NetworkAnimeDataRepository(apiService)
    }
    @Provides
    @Singleton
    fun provideFirebaseAuth() : FirebaseAuth {
        val firebaseAuth: FirebaseAuth = Firebase.auth
        return firebaseAuth
    }
    @Provides
    @Singleton
    fun provideFirebaseService(firebaseAuth: FirebaseAuth): FirebaseService {
        return FirebaseService(firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseService: FirebaseService) : AuthRepository {
        return  AuthRepositoryImp(firebaseService)
    }
    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }
    @Provides
    @Singleton
    fun provideUserPreferencesService(dataStore: DataStore<Preferences>) : UserPreferencesService{
        return UserPreferencesService(dataStore = dataStore)
    }
    @Provides
    @Singleton
    fun provideUserPreferences(userPreferencesService: UserPreferencesService) : UserPreferencesRepository {
        return  UserPreferencesImp(userPreferencesService)
    }
}