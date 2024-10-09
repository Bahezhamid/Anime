package com.example.domain.repository

import com.example.domain.entity.UsersData

interface AuthRepository {
    suspend fun login(email : String , password : String) : UsersData
    suspend fun signUp(email : String , password: String , userName : String) : UsersData
    suspend fun forgetPassword(email: String) : Boolean
    suspend fun signOut()
}