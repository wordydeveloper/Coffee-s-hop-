package com.example.coffeeshop.domain.usecase.order

import com.example.coffeeshop.data.model.Order
import com.example.coffeeshop.data.model.OrderStatus
import com.example.coffeeshop.domain.repository.IOrderRepository

class PlaceOrderUseCase(
    private val orderRepository: IOrderRepository
) {
    suspend operator fun invoke(
        coffeeName: String,
        quantity: Int,
        options: String
    ) {
        // ✅ IMPORTANTE: Generar ID único basado en timestamp
        val uniqueId = System.currentTimeMillis().toInt()

        val order = Order(
            id = uniqueId,  // En lugar de 0
            coffeeName = coffeeName,
            quantity = quantity,
            options = options,
            status = OrderStatus.PENDING,
            timestamp = System.currentTimeMillis()
        )

        orderRepository.insertOrder(order)
    }
}