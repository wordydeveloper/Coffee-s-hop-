package com.example.coffeeshop.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffeeshop.data.AppDatabase
import com.example.coffeeshop.data.Order
import com.example.coffeeshop.AppRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OrderViewModel(app: Application) : AndroidViewModel(app) {

    // Tu AppRepository recibe DAOs (OrderDao y FavoriteDao)
    private val db = AppDatabase.getInstance(app)
    private val repository = AppRepository(db.orderDao(), db.favoriteDao())

    // El repo expone Flow<List<Order>>
    val orders: StateFlow<List<Order>> =
        repository.orders.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    /** Crear pedido en Room */
    fun placeOrder(name: String, quantity: Int, options: String) {
        viewModelScope.launch {
            // Tu repo define placeOrder(coffeeName, quantity, options)
            repository.placeOrder(name, quantity, options)
        }
    }

    /** Cambiar estado del pedido */
    fun setStatus(order: Order, newStatus: String) {
        viewModelScope.launch {
            repository.updateOrderStatus(order, newStatus)
        }
    }

    /** Avanzar estado: Pendiente -> Preparando -> Listo para recoger */
    fun advanceStatus(order: Order) {
        val next = when (order.status) {
            "Pendiente" -> "Preparando"
            "Preparando" -> "Listo para recoger"
            else -> "Listo para recoger"
        }
        setStatus(order, next)
    }

    fun clearAll() {
        // Si luego agregas clearOrders en el repo, llámalo aquí
        viewModelScope.launch {
            // repository.clearOrders()
        }
    }
}
