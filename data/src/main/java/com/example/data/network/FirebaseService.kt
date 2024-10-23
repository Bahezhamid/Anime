package com.example.data.network
import android.util.Log
import com.example.domain.entity.FavoriteAnime
import com.example.domain.entity.SuccessesUpdateEmailAndPasswordData
import com.example.domain.entity.UpdateEmailData
import com.example.domain.entity.UpdatePasswordData
import com.example.domain.entity.UsersData
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseService(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
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

    suspend fun addAnimeToFavorite(favoriteAnime: FavoriteAnime) {

            firestore.collection("favorite").add(favoriteAnime)
                .addOnSuccessListener {
                    Log.d("Firestore", "Anime added to favorites!")
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Error adding document", e)
                }
    }

    suspend fun deleteAnimeFromFavorite(animeId : Int , userId: String) {

        val querySnapshot = firestore.collection("favorite")
            .whereEqualTo("animeId", animeId)
            .whereEqualTo("userId", userId)
            .get()
            .await()

        for (document in querySnapshot) {
            document.reference.delete()
        }
    }

    suspend fun getAnimeStatus(animeId: Int , userId : String) : Boolean {

        val doc = firestore.collection("favorite")
            .whereEqualTo("animeId", animeId)
            .whereEqualTo("userId", userId)
            .get()
            .await()
      return !doc.isEmpty
    }

    suspend fun getAllSavedAnime(userId: String): List<FavoriteAnime> {
        return withContext(Dispatchers.IO) {

            val querySnapshot = firestore.collection("favorite")
                .whereEqualTo("userId", userId)
                .get()
                .await()

            querySnapshot.documents.map { document ->
                FavoriteAnime(
                    animeId = document.getLong("animeId")?.toInt() ?: 0,
                    animePoster = document.getString("animePoster") ?: "",
                    animeName = document.getString("animeName") ?: "",
                    userId = document.getString("userId") ?: ""
                )
            }
        }
    }

    suspend fun getNumberOfAnimeAddedToFavorite(userId: String) : Int {
        val db = Firebase.firestore
        val querySnapshot = db.collection("favorite")
            .whereEqualTo("userId", userId)
            .get()
            .await()
        return querySnapshot.size()
    }


    @OptIn(DelicateCoroutinesApi::class)
    suspend fun updateEmailAddress(updateEmailData: UpdateEmailData): SuccessesUpdateEmailAndPasswordData {
        val user = FirebaseAuth.getInstance().currentUser

        return user?.let {
            val currentEmail = it.email
            val newEmail = updateEmailData.newEmail

            if (currentEmail == updateEmailData.oldEmail) {
                val credential = EmailAuthProvider.getCredential(currentEmail, updateEmailData.password)

                return@let suspendCancellableCoroutine { continuation ->
                    it.reauthenticate(credential).addOnCompleteListener { reauthTask ->
                        if (reauthTask.isSuccessful) {
                            try {
                                it.verifyBeforeUpdateEmail(newEmail)
                                    .addOnCompleteListener { verifyTask ->
                                        if (verifyTask.isSuccessful) {
                                            GlobalScope.launch {
                                                val emailUpdateResult = checkForEmailUpdateOnce(newEmail)
                                                continuation.resume(emailUpdateResult)
                                            }
                                        } else {
                                            continuation.resume(
                                                SuccessesUpdateEmailAndPasswordData(
                                                    isLoading = false,
                                                    isSuccess = false,
                                                    errorMessage = "Failed to send verification email"
                                                )
                                            )
                                        }
                                    }
                            } catch (e: Exception) {
                                continuation.resume(
                                    SuccessesUpdateEmailAndPasswordData(
                                        isLoading = false,
                                        isSuccess = false,
                                        errorMessage = e.localizedMessage ?: "Unknown error occurred"
                                    )
                                )
                            }
                        } else {
                            continuation.resume(
                                SuccessesUpdateEmailAndPasswordData(
                                    isLoading = false,
                                    isSuccess = false,
                                    errorMessage = "Wrong Email or Password"
                                )
                            )
                        }
                    }
                }
            } else {
                return SuccessesUpdateEmailAndPasswordData(
                    isLoading = false,
                    isSuccess = false,
                    errorMessage = "Current email does not match"
                )
            }
        } ?: SuccessesUpdateEmailAndPasswordData(
            isLoading = false,
            isSuccess = false,
            errorMessage = "User is not logged in"
        )
    }
    suspend fun updatePassword(updatePasswordData: UpdatePasswordData): SuccessesUpdateEmailAndPasswordData {
        val user = FirebaseAuth.getInstance().currentUser

        return user?.let {
            val currentEmail = it.email
            val currentPassword = updatePasswordData.oldPassword

            val credential = EmailAuthProvider.getCredential(currentEmail!!, currentPassword)

            return@let suspendCancellableCoroutine { continuation ->
                it.reauthenticate(credential).addOnCompleteListener { reauthTask ->
                    if (reauthTask.isSuccessful) {
                        try {
                            it.updatePassword(updatePasswordData.newPassword).addOnCompleteListener { updateTask ->
                                if (updateTask.isSuccessful) {
                                    continuation.resume(
                                        SuccessesUpdateEmailAndPasswordData(
                                            isLoading = false,
                                            isSuccess = true,
                                            errorMessage = "Password successfully updated"
                                        )
                                    )
                                } else {
                                    continuation.resume(
                                        SuccessesUpdateEmailAndPasswordData(
                                            isLoading = false,
                                            isSuccess = false,
                                            errorMessage = "Failed to update password"
                                        )
                                    )
                                }
                            }
                        } catch (e: Exception) {
                            continuation.resume(
                                SuccessesUpdateEmailAndPasswordData(
                                    isLoading = false,
                                    isSuccess = false,
                                    errorMessage = e.localizedMessage ?: "Unknown error occurred"
                                )
                            )
                        }
                    } else {
                        continuation.resume(
                            SuccessesUpdateEmailAndPasswordData(
                                isLoading = false,
                                isSuccess = false,
                                errorMessage = reauthTask.exception?.message ?: "Reauthentication failed"
                            )
                        )
                    }
                }
            }
        } ?: SuccessesUpdateEmailAndPasswordData(
            isLoading = false,
            isSuccess = false,
            errorMessage = "User is not logged in"
        )
    }

    private suspend fun checkForEmailUpdateOnce(newEmail: String): SuccessesUpdateEmailAndPasswordData {
        val user = FirebaseAuth.getInstance().currentUser
        val maxRetries = 15
        var retryCount = 0
        var isUpdateCompleted = false

        while (!isUpdateCompleted && retryCount < maxRetries) {
            user?.reload()?.addOnCompleteListener { reloadTask ->
                if (reloadTask.isSuccessful) {
                    if (user.email == newEmail) {
                        isUpdateCompleted = true
                        return@addOnCompleteListener
                    } else {
                        Log.d("EmailUpdate", "Email not updated yet, retrying...")
                    }
                } else {
                    isUpdateCompleted = true
                    return@addOnCompleteListener
                }
            }

            delay(2000L)
            retryCount++
        }

        return if (isUpdateCompleted) {
            SuccessesUpdateEmailAndPasswordData(
                isLoading = false,
                isSuccess = true,
                errorMessage = "Email successfully updated"
            )
        } else {
            SuccessesUpdateEmailAndPasswordData(
                isLoading = false,
                isSuccess = false,
                errorMessage = "Email failed to update after 30 seconds"
            )
        }
    }

}