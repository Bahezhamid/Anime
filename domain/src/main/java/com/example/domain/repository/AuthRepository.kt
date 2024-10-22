package com.example.domain.repository

import com.example.domain.entity.FavoriteAnime
import com.example.domain.entity.UsersData

interface AuthRepository {
    suspend fun login(email : String , password : String) : UsersData
    suspend fun signUp(email : String , password: String , userName : String) : UsersData
    suspend fun forgetPassword(email: String) : Boolean
    suspend fun signOut()
    suspend fun addAnimeToFavorite(favoriteAnime: FavoriteAnime)
    suspend fun deleteAnimeFromFavorite(animeId : Int , userId: String)
    suspend fun getAnimeStatus(animeId: Int , userId : String) :Boolean
    suspend fun getAllSavedAnime(userId: String) : List<FavoriteAnime>
}