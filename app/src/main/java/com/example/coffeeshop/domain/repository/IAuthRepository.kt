package com.example.coffeeshop.domain.repository


import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

sealed class AuthResult {
    data class Success(val user: FirebaseUser) : AuthResult()
    data class Error(val message: String) : AuthResult()
    object Loading : AuthResult()
}

interface IAuthRepository {
    val currentUser: FirebaseUser?

    suspend fun login(email: String, password: String): AuthResult
    suspend fun signup(name: String, email: String, password: String): AuthResult
    suspend fun loginWithGoogle(credential: AuthCredential): AuthResult
    suspend fun signOut()
    suspend fun updateFcmToken(token: String)
    fun observeAuthState(): Flow<FirebaseUser?>
}