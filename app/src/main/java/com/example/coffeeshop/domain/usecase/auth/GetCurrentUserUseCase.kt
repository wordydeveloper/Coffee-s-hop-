package com.example.coffeeshop.domain.usecase.auth

// GetCurrentUserUseCase.kt

import com.example.coffeeshop.domain.repository.IAuthRepository
import com.google.firebase.auth.FirebaseUser

class GetCurrentUserUseCase(
    private val authRepository: IAuthRepository
) {
    operator fun invoke(): FirebaseUser? {
        return authRepository.currentUser
    }
}