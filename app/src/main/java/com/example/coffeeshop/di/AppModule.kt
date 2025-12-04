package com.example.coffeeshop.di


import android.content.Context
import com.example.coffeeshop.data.local.database.AppDatabase
import com.example.coffeeshop.data.repository.*
import com.example.coffeeshop.domain.repository.*
import com.example.coffeeshop.domain.usecase.auth.*
import com.example.coffeeshop.domain.usecase.cart.*
import com.example.coffeeshop.domain.usecase.favorite.*
import com.example.coffeeshop.domain.usecase.order.*

/**
 * Módulo de inyección de dependencias manual
 * (Si más adelante quieres usar Hilt/Dagger, puedes refactorizar esto)
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

    val orderRepository: IOrderRepository by lazy {
        OrderRepository(orderDao)
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

    // ===== CART USE CASES =====
    val addToCartUseCase by lazy {
        AddToCartUseCase(cartRepository)
    }

    val getCartTotalUseCase by lazy {
        GetCartTotalUseCase(cartRepository)
    }

    // ===== FAVORITE USE CASES =====
    val getAllFavoritesUseCase by lazy {
        GetAllFavoritesUseCase(favoriteRepository)
    }

    val toggleFavoriteUseCase by lazy {
        ToggleFavoriteUseCase(favoriteRepository)
    }
}