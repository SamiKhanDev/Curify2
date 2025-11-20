package com.saim.domain.repositories

import com.google.firebase.auth.FirebaseUser

/**
 * Abstraction for authentication operations.
 * Keeping a thin contract enables easier testing and swapping auth backends.
 */
interface AuthRepositoryContract {
    suspend fun login(email: String, password: String): Result<FirebaseUser>
    suspend fun Signup(email: String, password: String, name: String): Result<FirebaseUser>
    suspend fun resetPassword(email: String): Result<Boolean>
    fun getCurrentUser(): FirebaseUser?
    fun logout(): Result<Boolean>
}

