package com.example.coffeeshop.ui.screens.cart

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class CartItem(
    val name: String,
    val price: Double
)

class CartViewModel : ViewModel() {

    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    val items: StateFlow<List<CartItem>> = _items

    private val _total = MutableStateFlow(0.0)
    val total: StateFlow<Double> = _total

    // Agregar al carrito
    fun add(item: CartItem) {
        _items.value = _items.value + item
        updateTotal()
    }

    // Quitar del carrito
    fun remove(item: CartItem) {
        _items.value = _items.value.filter { it.name != item.name }
        updateTotal()
    }

    // Vaciar carrito
    fun clear() {
        _items.value = emptyList()
        updateTotal()
    }

    private fun updateTotal() {
        _total.value = _items.value.sumOf { it.price }
    }
}
