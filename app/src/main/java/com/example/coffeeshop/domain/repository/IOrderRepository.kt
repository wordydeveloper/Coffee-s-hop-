package com.example.coffeeshop.domain.repository

import com.example.coffeeshop.data.model.Order
import com.example.coffeeshop.data.model.OrderStatus
import kotlinx.coroutines.flow.Flow

interface IOrderRepository {
    fun getAllOrders(): Flow<List<Order>>
    suspend fun insertOrder(order: Order)
    suspend fun updateOrderStatus(orderId: Int, status: OrderStatus)
    suspend fun deleteOrder(order: Order)
    suspend fun clearAllOrders()
}