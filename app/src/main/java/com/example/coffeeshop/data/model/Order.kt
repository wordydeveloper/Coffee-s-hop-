package com.example.coffeeshop.data.model

data class Order(
    val id: Int = 0,
    val coffeeName: String,
    val quantity: Int,
    val options: String,
    val status: OrderStatus,
    val timestamp: Long = System.currentTimeMillis()
)

enum class OrderStatus {
    PENDING,
    PREPARING,
    READY
}