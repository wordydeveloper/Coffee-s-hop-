package com.example.coffeeshop.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffeeshop.data.model.Order
import com.example.coffeeshop.data.model.OrderStatus
import com.example.coffeeshop.di.AppModule
import com.example.coffeeshop.domain.usecase.order.GetAllOrdersUseCase
import com.example.coffeeshop.domain.usecase.order.PlaceOrderUseCase
import com.example.coffeeshop.domain.usecase.order.UpdateOrderStatusUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OrderViewModel(
    private val getAllOrdersUseCase: GetAllOrdersUseCase = AppModule.getAllOrdersUseCase,
    private val placeOrderUseCase: PlaceOrderUseCase = AppModule.placeOrderUseCase,
    private val updateOrderStatusUseCase: UpdateOrderStatusUseCase = AppModule.updateOrderStatusUseCase
) : ViewModel() {

    /** Pedidos del usuario (vienen de Firebase a través del use case) */
    val orders: StateFlow<List<Order>> =
        getAllOrdersUseCase()
            .catch { e ->
                // ✅ IMPORTANTE: Manejar errores y emitir lista vacía
                Log.e("OrderViewModel", "Error al cargar pedidos: ${e.message}", e)
                emit(emptyList())
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    /** Crear pedido (se guarda en Firebase + Room según implemente el repo) */
    fun placeOrder(name: String, quantity: Int, options: String) {
        viewModelScope.launch {
            try {
                placeOrderUseCase(
                    coffeeName = name,
                    quantity = quantity,
                    options = options
                )
                Log.d("OrderViewModel", "✅ Pedido creado: $name x$quantity")
            } catch (e: Exception) {
                Log.e("OrderViewModel", "❌ Error al crear pedido: ${e.message}", e)
            }
        }
    }

    /** Cambiar el estado del pedido a un estado concreto */
    fun setStatus(order: Order, newStatus: OrderStatus) {
        viewModelScope.launch {
            try {
                updateOrderStatusUseCase(order.id, newStatus)
                Log.d("OrderViewModel", "✅ Estado actualizado: ${order.coffeeName} -> $newStatus")
            } catch (e: Exception) {
                Log.e("OrderViewModel", "❌ Error al actualizar estado: ${e.message}", e)
            }
        }
    }

    /** Avanzar estado: PENDING -> PREPARING -> READY */
    fun advanceStatus(order: Order) {
        val next = when (order.status) {
            OrderStatus.PENDING -> OrderStatus.PREPARING
            OrderStatus.PREPARING -> OrderStatus.READY
            OrderStatus.READY -> {
                Log.d("OrderViewModel", "Pedido ${order.coffeeName} ya está listo")
                return
            }
        }
        setStatus(order, next)
    }
}