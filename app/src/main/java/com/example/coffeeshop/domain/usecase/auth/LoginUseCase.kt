package com.example.coffeeshop.domain.usecase.auth

// LoginUseCase.kt

import com.example.coffeeshop.domain.repository.AuthResult
import com.example.coffeeshop.domain.repository.IAuthRepository

class LoginUseCase(
    private val authRepository: IAuthRepository
) {
    suspend operator fun invoke(email: String, password: String): AuthResult {
        if (email.isBlank() || password.isBlank()) {
            return AuthResult.Error("Email and password cannot be empty")
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return AuthResult.Error("Invalid email format")
        }

        return authRepository.login(email, password)
    }
}
