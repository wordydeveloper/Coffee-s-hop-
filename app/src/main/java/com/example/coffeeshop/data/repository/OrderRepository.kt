package com.example.coffeeshop.data.repository

import com.example.coffeeshop.data.local.dao.OrderDao
import com.example.coffeeshop.data.local.entity.OrderEntity
import com.example.coffeeshop.data.model.Order
import com.example.coffeeshop.data.model.OrderStatus
import com.example.coffeeshop.domain.repository.IOrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OrderRepository(
    private val orderDao: OrderDao
) : IOrderRepository {

    override fun getAllOrders(): Flow<List<Order>> {
        return orderDao.getAllOrders().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insertOrder(order: Order) {
        orderDao.insertOrder(order.toEntity())
    }

    override suspend fun updateOrderStatus(orderId: Int, status: OrderStatus) {
        val orders = orderDao.getAllOrders()
        // Implementar actualización
    }

    override suspend fun deleteOrder(order: Order) {
        orderDao.deleteOrder(order.toEntity())
    }

    override suspend fun clearAllOrders() {
        orderDao.clearOrders()
    }

    // Mappers
    private fun OrderEntity.toDomain() = Order(
        id = id,
        coffeeName = coffeeName,
        quantity = quantity,
        options = options,
        status = OrderStatus.valueOf(status),
        timestamp = timestamp
    )

    private fun Order.toEntity() = OrderEntity(
        id = id,
        coffeeName = coffeeName,
        quantity = quantity,
        options = options,
        status = status.name,
        timestamp = timestamp
    )
}