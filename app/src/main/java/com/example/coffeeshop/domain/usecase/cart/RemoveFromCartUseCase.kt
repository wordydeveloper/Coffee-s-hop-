package com.example.coffeeshop.domain.usecase.cart


import com.example.coffeeshop.data.model.Coffee
import com.example.coffeeshop.domain.repository.ICartRepository

class RemoveFromCartUseCase(
    private val cartRepository: ICartRepository
) {
    suspend operator fun invoke(coffee: Coffee) {
        cartRepository.removeFromCart(coffee)
    }
}
