package com.example.data.repository

import com.example.data.network.FirebaseService
import com.example.domain.entity.FavoriteAnime
import com.example.domain.entity.SuccessesUpdateEmailAndPasswordData
import com.example.domain.entity.UpdateEmailData
import com.example.domain.entity.UpdatePasswordData
import com.example.domain.entity.UsersData
import com.example.domain.repository.AuthRepository

class AuthRepositoryImp(
    private val firebaseService: FirebaseService
) :AuthRepository  {
    override suspend fun login(email: String, password: String ): UsersData =
        firebaseService.login(email = email , password = password)
    override suspend fun signUp(email: String, password: String, userName: String): UsersData =
        firebaseService.signUp(email = email, password = password, userName = userName)
    override suspend fun forgetPassword(email: String): Boolean = firebaseService.forgetPassword(email = email)
    override suspend fun signOut()  = firebaseService.signOut()
    override suspend fun addAnimeToFavorite(favoriteAnime: FavoriteAnime) =
        firebaseService.addAnimeToFavorite(favoriteAnime = favoriteAnime)
    override suspend fun deleteAnimeFromFavorite(animeId: Int , userId: String) =
        firebaseService.deleteAnimeFromFavorite(animeId = animeId , userId = userId)
    override suspend fun getAnimeStatus(animeId: Int, userId: String) : Boolean =
        firebaseService.getAnimeStatus(animeId = animeId , userId = userId)
    override suspend fun getAllSavedAnime(userId: String): List<FavoriteAnime> =
        firebaseService.getAllSavedAnime(userId = userId)
    override suspend fun getNumberOfAddedAnimeToFavorite(userId: String): Int =
        firebaseService.getNumberOfAnimeAddedToFavorite(userId = userId)
    override suspend fun updateEmailAddress(updateEmailData: UpdateEmailData): SuccessesUpdateEmailAndPasswordData  =
        firebaseService.updateEmailAddress(updateEmailData = updateEmailData)

    override suspend fun updatePassword(updatePasswordData: UpdatePasswordData): SuccessesUpdateEmailAndPasswordData  =
        firebaseService.updatePassword(updatePasswordData = updatePasswordData)
}