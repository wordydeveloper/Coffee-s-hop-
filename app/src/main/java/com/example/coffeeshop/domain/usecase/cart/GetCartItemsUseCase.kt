package com.example.coffeeshop.domain.usecase.cart

import com.example.coffeeshop.data.model.CartItem
import com.example.coffeeshop.domain.repository.ICartRepository
import kotlinx.coroutines.flow.Flow

class GetCartItemsUseCase(
    private val cartRepository: ICartRepository
) {
    operator fun invoke(): Flow<List<CartItem>> {
        return cartRepository.getCartItems()
    }
}
