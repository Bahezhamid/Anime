package com.example.domain.repository

import com.example.domain.entity.FavoriteAnime
import com.example.domain.entity.SuccessesUpdateEmailAndPasswordData
import com.example.domain.entity.UpdateEmailData
import com.example.domain.entity.UpdatePasswordData
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
    suspend fun getNumberOfAddedAnimeToFavorite(userId: String) : Int
    suspend fun updateEmailAddress(updateEmailData: UpdateEmailData) : SuccessesUpdateEmailAndPasswordData
    suspend fun updatePassword(updatePasswordData: UpdatePasswordData) : SuccessesUpdateEmailAndPasswordData
}