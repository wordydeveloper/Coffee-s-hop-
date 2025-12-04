package com.example.coffeeshop.di.presentation.ui.screens.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CartItem(
    val id: String = "",
    val name: String,
    val price: Double,
    val quantity: Int = 1,
    val options: List<String> = emptyList()
) {
    val totalPrice: Double
        get() = price * quantity
}

sealed class CartUiState {
    object Loading : CartUiState()
    data class Success(
        val items: List<CartItem>,
        val total: Double
    ) : CartUiState()
    object Empty : CartUiState()
    data class Error(val message: String) : CartUiState()
}

class CartViewModel : ViewModel() {

    // Estado interno (privado)
    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    val items: StateFlow<List<CartItem>> = _items.asStateFlow()

    private val _total = MutableStateFlow(0.0)
    val total: StateFlow<Double> = _total.asStateFlow()

    private val _uiState = MutableStateFlow<CartUiState>(CartUiState.Empty)
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    // Agregar al carrito
    fun addItem(item: CartItem) {
        viewModelScope.launch {
            try {
                val currentItems = _items.value.toMutableList()
                val existingIndex = currentItems.indexOfFirst { it.id == item.id }

                if (existingIndex != -1) {
                    // Si ya existe, incrementar cantidad
                    val existing = currentItems[existingIndex]
                    currentItems[existingIndex] = existing.copy(
                        quantity = existing.quantity + item.quantity
                    )
                } else {
                    // Si no existe, agregar nuevo
                    currentItems.add(item)
                }

                _items.value = currentItems
                updateTotal()
                updateUiState()
            } catch (e: Exception) {
                _uiState.value = CartUiState.Error("Error al agregar item: ${e.message}")
            }
        }
    }

    // Quitar del carrito por ID
    fun removeItem(itemId: String) {
        viewModelScope.launch {
            try {
                _items.value = _items.value.filter { it.id != itemId }
                updateTotal()
                updateUiState()
            } catch (e: Exception) {
                _uiState.value = CartUiState.Error("Error al eliminar item: ${e.message}")
            }
        }
    }

    // Quitar del carrito por nombre (compatibilidad)
    fun removeItemByName(name: String) {
        viewModelScope.launch {
            try {
                _items.value = _items.value.filter { it.name != name }
                updateTotal()
                updateUiState()
            } catch (e: Exception) {
                _uiState.value = CartUiState.Error("Error al eliminar item: ${e.message}")
            }
        }
    }

    // Actualizar cantidad de un item
    fun updateQuantity(itemId: String, newQuantity: Int) {
        viewModelScope.launch {
            try {
                if (newQuantity <= 0) {
                    removeItem(itemId)
                    return@launch
                }

                _items.value = _items.value.map { item ->
                    if (item.id == itemId) {
                        item.copy(quantity = newQuantity)
                    } else {
                        item
                    }
                }
                updateTotal()
                updateUiState()
            } catch (e: Exception) {
                _uiState.value = CartUiState.Error("Error al actualizar cantidad: ${e.message}")
            }
        }
    }

    // Vaciar carrito
    fun clearCart() {
        viewModelScope.launch {
            try {
                _items.value = emptyList()
                _total.value = 0.0
                _uiState.value = CartUiState.Empty
            } catch (e: Exception) {
                _uiState.value = CartUiState.Error("Error al vaciar carrito: ${e.message}")
            }
        }
    }

    // Obtener cantidad de items
    fun getItemCount(): Int = _items.value.sumOf { it.quantity }

    // Verificar si el carrito está vacío
    fun isEmpty(): Boolean = _items.value.isEmpty()

    // Actualizar el total
    private fun updateTotal() {
        _total.value = _items.value.sumOf { it.totalPrice }
    }

    // Actualizar el estado de UI
    private fun updateUiState() {
        _uiState.value = if (_items.value.isEmpty()) {
            CartUiState.Empty
        } else {
            CartUiState.Success(
                items = _items.value,
                total = _total.value
            )
        }
    }

    // Función para aplicar descuentos (opcional)
    fun applyDiscount(percentage: Double): Double {
        val discount = _total.value * (percentage / 100)
        return _total.value - discount
    }

    // Obtener resumen del carrito
    fun getCartSummary(): String {
        return buildString {
            append("Total de items: ${getItemCount()}\n")
            append("Total a pagar: $${String.format("%.2f", _total.value)}")
        }
    }
}