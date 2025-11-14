package com.example.coffeeshop.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffeeshop.data.model.Coffee
import com.example.coffeeshop.domain.usecase.cart.AddToCartUseCase
import com.example.coffeeshop.domain.usecase.favorite.GetAllFavoritesUseCase
import com.example.coffeeshop.domain.usecase.favorite.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SearchViewModel(
    private val addToCartUseCase: AddToCartUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val getAllFavoritesUseCase: GetAllFavoritesUseCase  // ⚡ Agregar 'private val'
) : ViewModel() {

    // ⚡ Ahora invoca el use case correctamente
    val favorites: StateFlow<List<String>> = getAllFavoritesUseCase.invoke()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addToCart(coffee: Coffee) {
        viewModelScope.launch {
            addToCartUseCase(coffee)  // o addToCartUseCase.invoke(coffee)
        }
    }

    fun toggleFavorite(coffeeName: String) {
        viewModelScope.launch {
            toggleFavoriteUseCase(coffeeName)  // o toggleFavoriteUseCase.invoke(coffeeName)
        }
    }
}
