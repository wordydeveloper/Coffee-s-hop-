package com.example.coffeeshop.domain.usecase.cart

import com.example.coffeeshop.domain.repository.ICartRepository
import kotlinx.coroutines.flow.Flow

class GetCartTotalUseCase(
    private val cartRepository: ICartRepository
) {
    operator fun invoke(): Flow<Double> {
        return cartRepository.getCartTotal()
    }
}