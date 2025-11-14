package com.example.coffeeshop.data.repository

import com.example.coffeeshop.data.model.CartItem
import com.example.coffeeshop.data.model.Coffee
import com.example.coffeeshop.domain.repository.ICartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class CartRepository : ICartRepository {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())

    override fun getCartItems(): Flow<List<CartItem>> = _cartItems

    override suspend fun addToCart(coffee: Coffee) {
        val currentList = _cartItems.value.toMutableList()
        val existingItem = currentList.find { it.coffee.id == coffee.id }

        if (existingItem != null) {
            val index = currentList.indexOf(existingItem)
            currentList[index] = existingItem.copy(quantity = existingItem.quantity + 1)
        } else {
            currentList.add(CartItem(coffee = coffee))
        }

        _cartItems.value = currentList
    }

    override suspend fun removeFromCart(coffee: Coffee) {
        _cartItems.value = _cartItems.value.filter { it.coffee.id != coffee.id }
    }

    override suspend fun updateCartItem(cartItem: CartItem) {
        val currentList = _cartItems.value.toMutableList()
        val index = currentList.indexOfFirst { it.coffee.id == cartItem.coffee.id }

        if (index != -1) {
            currentList[index] = cartItem
            _cartItems.value = currentList
        }
    }

    override suspend fun clearCart() {
        _cartItems.value = emptyList()
    }

    override fun getCartTotal(): Flow<Double> {
        return _cartItems.map { items ->
            items.sumOf { it.totalPrice }
        }
    }
}