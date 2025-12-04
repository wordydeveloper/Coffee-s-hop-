package com.example.coffeeshop.di.presentation.ui.screens.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffeeshop.data.model.Order
import com.example.coffeeshop.data.model.OrderStatus
import com.example.coffeeshop.di.AppModule
import com.example.coffeeshop.domain.usecase.cart.ClearCartUseCase
import com.example.coffeeshop.domain.usecase.cart.GetCartItemsUseCase
import com.example.coffeeshop.domain.usecase.cart.GetCartTotalUseCase
import com.example.coffeeshop.domain.usecase.cart.RemoveFromCartUseCase
import com.example.coffeeshop.domain.usecase.order.GetAllOrdersUseCase
import com.example.coffeeshop.domain.usecase.order.PlaceOrderUseCase
import com.example.coffeeshop.domain.usecase.order.UpdateOrderStatusUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private const val TAG = "CartViewModel"

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
        val total: Double,
        val orders: List<Order>
    ) : CartUiState()
    object Empty : CartUiState()
    data class Error(val message: String) : CartUiState()
}

class CartViewModel(
    private val getCartItemsUseCase: GetCartItemsUseCase = AppModule.getCartItemsUseCase,
    private val getCartTotalUseCase: GetCartTotalUseCase = AppModule.getCartTotalUseCase,
    private val removeFromCartUseCase: RemoveFromCartUseCase = AppModule.removeFromCartUseCase,
    private val clearCartUseCase: ClearCartUseCase = AppModule.clearCartUseCase,
    private val placeOrderUseCase: PlaceOrderUseCase = AppModule.placeOrderUseCase,
    private val getAllOrdersUseCase: GetAllOrdersUseCase = AppModule.getAllOrdersUseCase,
    private val updateOrderStatusUseCase: UpdateOrderStatusUseCase = AppModule.updateOrderStatusUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<CartUiState>(CartUiState.Loading)
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    init {
        loadCart()
    }

    private fun loadCart() {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Cargando carrito y pedidos...")
                combine(
                    getCartItemsUseCase(),
                    getCartTotalUseCase(),
                    getAllOrdersUseCase()
                ) { cartItems, total, orders ->
                    if (cartItems.isEmpty() && orders.isEmpty()) {
                        CartUiState.Empty
                    } else {
                        CartUiState.Success(
                            items = cartItems.map { it.toCartItem() },
                            total = total,
                            orders = orders
                        )
                    }
                }.collect { state ->
                    _uiState.value = state
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error al cargar el carrito", e)
                _uiState.value =
                    CartUiState.Error("Error al cargar el carrito: ${e.message}")
            }
        }
    }

    fun removeItem(itemId: String) {
        viewModelScope.launch {
            try {
                val currentState = _uiState.value
                if (currentState is CartUiState.Success) {
                    val itemToRemove = currentState.items.find { it.id == itemId }
                    if (itemToRemove != null) {
                        val coffee = com.example.coffeeshop.data.model.Coffee(
                            id = itemToRemove.id,
                            imageRes = 0, // No necesitamos la imagen
                            name = itemToRemove.name,
                            description = "",
                            price = itemToRemove.price
                        )
                        removeFromCartUseCase(coffee)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error al eliminar del carrito", e)
                _uiState.value =
                    CartUiState.Error("Error al eliminar: ${e.message}")
            }
        }
    }

    fun updateQuantity(itemId: String, newQuantity: Int) {
        viewModelScope.launch {
            try {
                if (newQuantity <= 0) {
                    removeItem(itemId)
                    return@launch
                }

                val currentState = _uiState.value
                if (currentState is CartUiState.Success) {
                    val item = currentState.items.find { it.id == itemId }
                    if (item != null) {
                        val cartItem = com.example.coffeeshop.data.model.CartItem(
                            coffee = com.example.coffeeshop.data.model.Coffee(
                                id = item.id,
                                imageRes = 0,
                                name = item.name,
                                description = "",
                                price = item.price
                            ),
                            quantity = newQuantity,
                            addSugar = item.options.contains("azúcar"),
                            addMilk = item.options.contains("leche")
                        )
                        // Actualizar usando el repositorio directamente
                        AppModule.cartRepository.updateCartItem(cartItem)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error al actualizar cantidad", e)
                _uiState.value =
                    CartUiState.Error("Error al actualizar: ${e.message}")
            }
        }
    }

    fun checkout() {
        viewModelScope.launch {
            try {
                val currentState = _uiState.value
                if (currentState !is CartUiState.Success || currentState.items.isEmpty()) {
                    Log.w(TAG, "checkout() llamado sin items en el carrito")
                    return@launch
                }

                Log.d(TAG, "Iniciando checkout con ${currentState.items.size} items")

                currentState.items.forEach { item ->
                    val options = if (item.options.isNotEmpty()) {
                        item.options.joinToString(", ")
                    } else {
                        ""
                    }

                    Log.d(
                        TAG,
                        "Creando pedido en Firebase: ${item.name} x${item.quantity} [$options]"
                    )

                    placeOrderUseCase(
                        coffeeName = item.name,
                        quantity = item.quantity,
                        options = options
                    )
                }

                // Limpiar el carrito local
                clearCartUseCase()
                Log.d(TAG, "Carrito limpiado después de checkout")

            } catch (e: Exception) {
                Log.e(TAG, "Error en checkout", e)
                _uiState.value =
                    CartUiState.Error("Error al procesar el pedido: ${e.message}")
            }
        }
    }

    fun advanceOrderStatus(order: Order) {
        viewModelScope.launch {
            try {
                val nextStatus = when (order.status) {
                    OrderStatus.PENDING -> OrderStatus.PREPARING
                    OrderStatus.PREPARING -> OrderStatus.READY
                    OrderStatus.READY -> return@launch // Ya está listo
                }
                updateOrderStatusUseCase(order.id, nextStatus)
            } catch (e: Exception) {
                Log.e(TAG, "Error al avanzar estado", e)
                _uiState.value =
                    CartUiState.Error("Error al actualizar estado: ${e.message}")
            }
        }
    }

    fun updateOrderStatus(order: Order, newStatus: OrderStatus) {
        viewModelScope.launch {
            try {
                updateOrderStatusUseCase(order.id, newStatus)
            } catch (e: Exception) {
                Log.e(TAG, "Error al actualizar estado manualmente", e)
                _uiState.value =
                    CartUiState.Error("Error al actualizar estado: ${e.message}")
            }
        }
    }

    // Mapper desde CartItem de dominio a CartItem de UI
    private fun com.example.coffeeshop.data.model.CartItem.toCartItem() = CartItem(
        id = coffee.id,
        name = coffee.name,
        price = coffee.price,
        quantity = quantity,
        options = buildList {
            if (addSugar) add("azúcar")
            if (addMilk) add("leche")
        }
    )
}
