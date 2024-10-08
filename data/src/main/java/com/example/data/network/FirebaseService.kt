package com.example.data.network
import com.example.domain.entity.UsersData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseService(
    private val firebaseAuth: FirebaseAuth
) {
    suspend fun login(email: String, password: String): UsersData {
        try {
            val authResult = withContext(Dispatchers.IO) {
                firebaseAuth.signInWithEmailAndPassword(email, password).await()
            }

            val firebaseUser = firebaseAuth.currentUser

            if (firebaseUser != null) {
                val userId = firebaseUser.uid
                val userDocument =
                    Firebase.firestore.collection("users").document(userId).get().await()
                if (userDocument.exists()) {
                    val userData = userDocument.data
                    val userName = userData?.get("userName") as? String ?: ""
                    return UsersData(
                        userId = userId,
                        email = email,
                        userName = userName,
                        isSuccess = true
                    )
                } else {
                    return UsersData(
                        isSuccess = false,
                        errorMessage = "Wrong Email or Password"
                    )
                }
            } else {
                return UsersData(
                    errorMessage = "Wrong Email or Password",
                    isSuccess = false,
                )
            }
        } catch (e: Exception) {
            return UsersData(
                errorMessage = "Wrong Email Or Password",
                isSuccess = false,
            )
        }
    }

    suspend fun signUp(email: String, password: String , userName : String) : UsersData {
        try {
            val authResult = withContext(Dispatchers.IO) {
                firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            }

            val firebaseUser = firebaseAuth.currentUser
            val userId = firebaseUser?.uid

            if (userId != null) {

                val user = hashMapOf(
                    "id" to userId,
                    "userName" to userName,
                    "email" to email,
                )
                val db = Firebase.firestore
                db.collection("users").document(userId)
                    .set(user)
                    .await()
                return UsersData(
                    userId = userId,
                    email = email,
                    userName = userName,
                    isSuccess = true
                )
            }
            else {
                return UsersData(
                    isSuccess = false,
                    errorMessage = "Not Valid Email"
                )
            }
        } catch (e: Exception) {
            return UsersData(
                isSuccess = false,
                errorMessage = "Not Valid Email"
            )
        }
    }

     suspend fun forgetPassword(email: String): Boolean {
         return suspendCoroutine { continuation ->
             FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                 .addOnCompleteListener { task ->
                     if (task.isSuccessful) {
                         continuation.resume(true)
                     } else {
                         continuation.resume(false)
                     }
                 }
         }
     }
    suspend fun signOut() {
       firebaseAuth.signOut()
    }
}