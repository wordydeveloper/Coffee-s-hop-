package com.example.coffeeshop.data.model

import com.google.firebase.auth.FirebaseUser

sealed class AuthState {
    data class Authenticated(val user: FirebaseUser?) : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}

sealed interface AuthEvent {
    data class Navigate(val route: String) : AuthEvent
    data class ShowToast(val message: String) : AuthEvent
}