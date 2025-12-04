package com.example.coffeeshop.di

import android.content.Context
import com.example.coffeeshop.data.local.database.AppDatabase
import com.example.coffeeshop.data.repository.AuthRepository
import com.example.coffeeshop.data.repository.CartRepository
import com.example.coffeeshop.data.repository.FavoriteRepository
import com.example.coffeeshop.data.repository.FirebaseOrderRepository
import com.example.coffeeshop.domain.repository.*
import com.example.coffeeshop.domain.usecase.auth.*
import com.example.coffeeshop.domain.usecase.cart.*
import com.example.coffeeshop.domain.usecase.favorite.*
import com.example.coffeeshop.domain.usecase.order.*

/**
 * Módulo de inyección de dependencias manual
 * Ahora con soporte para Firebase en pedidos
 */
object AppModule {

    // ===== DATABASE =====
    private lateinit var database: AppDatabase

    fun initializeDatabase(context: Context) {
        database = AppDatabase.getInstance(context)
    }

    // ===== DATA SOURCES =====
    private val orderDao by lazy { database.orderDao() }
    private val favoriteDao by lazy { database.favoriteDao() }

    // ===== REPOSITORIES =====
    val authRepository: IAuthRepository by lazy {
        AuthRepository()
    }

    // 🔥 Repositorio de pedidos basado en Firebase
    val orderRepository: IOrderRepository by lazy {
        // forzamos a que el Lazy sea de tipo IOrderRepository
        FirebaseOrderRepository(orderDao) as IOrderRepository
    }

    val favoriteRepository: IFavoriteRepository by lazy {
        FavoriteRepository(favoriteDao)
    }

    val cartRepository: ICartRepository by lazy {
        CartRepository()
    }

    // ===== AUTH USE CASES =====
    val loginUseCase by lazy {
        LoginUseCase(authRepository)
    }

    val signupUseCase by lazy {
        SignupUseCase(authRepository)
    }

    val loginWithGoogleUseCase by lazy {
        LoginWithGoogleUseCase(authRepository)
    }

    val signOutUseCase by lazy {
        SignOutUseCase(authRepository)
    }

    val getCurrentUserUseCase by lazy {
        GetCurrentUserUseCase(authRepository)
    }

    val observeAuthStateUseCase by lazy {
        ObserveAuthStateUseCase(authRepository)
    }

    // ===== ORDER USE CASES =====
    val placeOrderUseCase by lazy {
        PlaceOrderUseCase(orderRepository)
    }

    val updateOrderStatusUseCase by lazy {
        UpdateOrderStatusUseCase(orderRepository)
    }

    val getAllOrdersUseCase by lazy {
        GetAllOrdersUseCase(orderRepository)
    }

    // ===== CART USE CASES =====
    val addToCartUseCase by lazy {
        AddToCartUseCase(cartRepository)
    }

    val getCartTotalUseCase by lazy {
        GetCartTotalUseCase(cartRepository)
    }

    val getCartItemsUseCase by lazy {
        GetCartItemsUseCase(cartRepository)
    }

    val removeFromCartUseCase by lazy {
        RemoveFromCartUseCase(cartRepository)
    }

    val clearCartUseCase by lazy {
        ClearCartUseCase(cartRepository)
    }

    // ===== FAVORITE USE CASES =====
    val getAllFavoritesUseCase by lazy {
        GetAllFavoritesUseCase(favoriteRepository)
    }

    val toggleFavoriteUseCase by lazy {
        ToggleFavoriteUseCase(favoriteRepository)
    }
}
