package com.saim.data.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import com.saim.domain.repositories.AuthRepositoryContract

class AuthRepository : AuthRepositoryContract {

    override suspend fun login(email: String, password: String): Result<FirebaseUser> {
        try {
            val result =
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).await()
            return Result.success(result.user!!)

        } catch (e: Exception) {
            return Result.failure(e)

        }
    }

    override suspend fun Signup(email: String, password: String, name: String): Result<FirebaseUser> {
        try {
            val result =
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).await()
            val profileUpdates = userProfileChangeRequest {
                displayName = name

            }


            result.user?.updateProfile(profileUpdates)?.await()
            return Result.success(result.user!!)

        } catch (e: Exception) {
            return Result.failure(e)

        }
    }

    override suspend fun resetPassword(email: String): Result<Boolean> {
        try {
            val result = FirebaseAuth.getInstance().sendPasswordResetEmail(email).await()
            return Result.success(true)


        } catch (e: Exception) {
            return Result.failure(e)

        }
    }

    override fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    override fun logout(): Result<Boolean> {

        try {

            val result = Firebase.auth.signOut()
            return Result.success(true)
        }


        catch (e: Exception) {
            return Result.failure(e)

        }

    }

}