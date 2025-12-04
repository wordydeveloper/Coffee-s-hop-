package com.example.coffeeshop.domain.usecase.auth

// ObserveAuthStateUseCase.kt
import com.example.coffeeshop.domain.repository.IAuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

class ObserveAuthStateUseCase(
    private val authRepository: IAuthRepository
) {
    operator fun invoke(): Flow<FirebaseUser?> {
        return authRepository.observeAuthState()
    }
}