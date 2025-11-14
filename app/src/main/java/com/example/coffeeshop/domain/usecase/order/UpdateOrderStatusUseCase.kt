package com.example.coffeeshop.domain.usecase.order

import com.example.coffeeshop.data.model.OrderStatus
import com.example.coffeeshop.domain.repository.IOrderRepository

class UpdateOrderStatusUseCase(
    private val orderRepository: IOrderRepository
) {
    suspend operator fun invoke(orderId: Int, newStatus: OrderStatus) {
        orderRepository.updateOrderStatus(orderId, newStatus)
    }
}