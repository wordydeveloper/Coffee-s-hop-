package com.example.coffeeshop.domain.repository

import com.example.coffeeshop.data.model.CartItem
import com.example.coffeeshop.data.model.Coffee
import kotlinx.coroutines.flow.Flow

interface ICartRepository {
    fun getCartItems(): Flow<List<CartItem>>
    suspend fun addToCart(coffee: Coffee)
    suspend fun removeFromCart(coffee: Coffee)
    suspend fun updateCartItem(cartItem: CartItem)
    suspend fun clearCart()
    fun getCartTotal(): Flow<Double>
}
