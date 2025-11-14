package com.example.coffeeshop.ui.screens.viewmodel
/*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.coffeeshop.di.AppModule
import com.example.coffeeshop.ui.screens.cart.CartViewModel
import com.example.coffeeshop.ui.screens.favorite.FavoriteViewModel
import com.example.coffeeshop.ui.screens.home.HomeViewModel
import com.example.coffeeshop.ui.screens.profile.ProfileViewModel
import com.example.coffeeshop.ui.screens.search.SearchViewModel

class ViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(
                    placeOrderUseCase = AppModule.placeOrderUseCase,
                    addToCartUseCase = AppModule.addToCartUseCase
                ) as T
            }

            modelClass.isAssignableFrom(CartViewModel::class.java) -> {
                CartViewModel(
                    getCartItemsUseCase = AppModule.getCartItemsUseCase,
                    getCartTotalUseCase = AppModule.getCartTotalUseCase,
                    removeFromCartUseCase = AppModule.removeFromCartUseCase,
                    checkoutUseCase = AppModule.checkoutUseCase,
                    getAllOrdersUseCase = AppModule.getAllOrdersUseCase,
                    advanceOrderStatusUseCase = AppModule.advanceOrderStatusUseCase,
                    updateOrderStatusUseCase = AppModule.updateOrderStatusUseCase
                ) as T
            }

            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                SearchViewModel(
                    addToCartUseCase = AppModule.addToCartUseCase,
                    toggleFavoriteUseCase = AppModule.toggleFavoriteUseCase,
                    getAllFavoritesUseCase = AppModule.getAllFavoritesUseCase
                ) as T
            }

            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                FavoriteViewModel(
                    getAllFavoritesUseCase = AppModule.getAllFavoritesUseCase,
                    toggleFavoriteUseCase = AppModule.toggleFavoriteUseCase
                ) as T
            }

            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(
                    getAllOrdersUseCase = AppModule.getAllOrdersUseCase
                ) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
*/