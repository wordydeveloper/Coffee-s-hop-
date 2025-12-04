package com.example.coffeeshop.domain.usecase.cart


import com.example.coffeeshop.domain.repository.ICartRepository

class ClearCartUseCase(
    private val cartRepository: ICartRepository
) {
    suspend operator fun invoke() {
        cartRepository.clearCart()
    }
}