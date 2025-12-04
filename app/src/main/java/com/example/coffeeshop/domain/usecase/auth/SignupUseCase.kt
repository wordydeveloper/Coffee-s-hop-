package com.example.coffeeshop.domain.usecase.auth

// SignupUseCase.kt


import com.example.coffeeshop.domain.repository.AuthResult
import com.example.coffeeshop.domain.repository.IAuthRepository

class SignupUseCase(
    private val authRepository: IAuthRepository
) {
    suspend operator fun invoke(name: String, email: String, password: String): AuthResult {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            return AuthResult.Error("All fields are required")
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return AuthResult.Error("Invalid email format")
        }

        if (password.length < 6) {
            return AuthResult.Error("Password must be at least 6 characters")
        }

        return authRepository.signup(name, email, password)
    }
}