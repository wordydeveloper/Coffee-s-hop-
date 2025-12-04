package com.example.coffeeshop.viewmodel

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
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    /** Crear pedido (se guarda en Firebase + Room según implemente el repo) */
    fun placeOrder(name: String, quantity: Int, options: String) {
        viewModelScope.launch {
            placeOrderUseCase(
                coffeeName = name,
                quantity = quantity,
                options = options
            )
        }
    }

    /** Cambiar el estado del pedido a un estado concreto */
    fun setStatus(order: Order, newStatus: OrderStatus) {
        viewModelScope.launch {
            updateOrderStatusUseCase(order.id, newStatus)
        }
    }

    /** Avanzar estado: PENDING -> PREPARING -> READY */
    fun advanceStatus(order: Order) {
        val next = when (order.status) {
            OrderStatus.PENDING -> OrderStatus.PREPARING
            OrderStatus.PREPARING -> OrderStatus.READY
            OrderStatus.READY -> return   // ya está listo, no avanza más
        }
        setStatus(order, next)
    }
}
