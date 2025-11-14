package com.example.coffeeshop.data.model

data class CartItem(
    val coffee: Coffee,
    val quantity: Int = 1,
    val addSugar: Boolean = false,
    val addMilk: Boolean = false
) {
    val totalPrice: Double
        get() = coffee.price * quantity
}
