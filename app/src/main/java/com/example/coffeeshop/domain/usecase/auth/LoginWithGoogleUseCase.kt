package com.example.coffeeshop.domain.usecase.auth

// LoginWithGoogleUseCase.kt

import com.example.coffeeshop.domain.repository.AuthResult
import com.example.coffeeshop.domain.repository.IAuthRepository
import com.google.firebase.auth.AuthCredential

class LoginWithGoogleUseCase(
    private val authRepository: IAuthRepository
) {
    suspend operator fun invoke(credential: AuthCredential): AuthResult {
        return authRepository.loginWithGoogle(credential)
    }
}