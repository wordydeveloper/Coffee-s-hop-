package com.example.coffeeshop.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffeeshop.data.model.Coffee
import com.example.coffeeshop.domain.usecase.cart.AddToCartUseCase
import com.example.coffeeshop.domain.usecase.order.PlaceOrderUseCase
import kotlinx.coroutines.launch

class HomeViewModel(
    private val placeOrderUseCase: PlaceOrderUseCase,
    private val addToCartUseCase: AddToCartUseCase
) : ViewModel() {

    fun placeOrder(coffeeName: String, quantity: Int, options: String) {
        viewModelScope.launch {
            placeOrderUseCase(coffeeName, quantity, options)
        }
    }

    fun addToCart(coffee: Coffee) {
        viewModelScope.launch {
            addToCartUseCase(coffee)
        }
    }
}