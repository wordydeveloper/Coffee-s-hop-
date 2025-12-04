package com.example.coffeeshop.domain.usecase.order


import com.example.coffeeshop.data.model.Order
import com.example.coffeeshop.domain.repository.IOrderRepository
import kotlinx.coroutines.flow.Flow

class GetAllOrdersUseCase(
    private val orderRepository: IOrderRepository
) {
    operator fun invoke(): Flow<List<Order>> {
        return orderRepository.getAllOrders()
    }
}
