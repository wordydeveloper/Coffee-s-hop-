package com.example.coffeeshop.domain.usecase.auth

// SignOutUseCase.kt

import com.example.coffeeshop.domain.repository.IAuthRepository

class SignOutUseCase(
    private val authRepository: IAuthRepository
) {
    suspend operator fun invoke() {
        authRepository.signOut()
    }
}