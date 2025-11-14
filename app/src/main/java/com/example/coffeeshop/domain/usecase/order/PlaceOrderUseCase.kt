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
        val order = Order(
            coffeeName = coffeeName,
            quantity = quantity,
            options = options,
            status = OrderStatus.PENDING
        )
        orderRepository.insertOrder(order)
    }
}