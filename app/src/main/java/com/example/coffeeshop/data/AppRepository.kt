package com.example.coffeeshop


import com.example.coffeeshop.data.Favorite
import com.example.coffeeshop.data.FavoriteDao
import com.example.coffeeshop.data.Order
import com.example.coffeeshop.data.OrderDao
import kotlinx.coroutines.flow.Flow

class AppRepository(
    private val orderDao: OrderDao,
    private val favoriteDao: FavoriteDao
) {
    val orders: Flow<List<Order>> = orderDao.getAllOrders()
    val favorites: Flow<List<Favorite>> = favoriteDao.getFavorites()

    suspend fun placeOrder(coffeeName: String, quantity: Int, options: String) {
        val order = Order(
            coffeeName = coffeeName,
            quantity = quantity,
            options = options,
            status = "Pendiente"
        )
        orderDao.insertOrder(order)
    }

    suspend fun updateOrderStatus(order: Order, newStatus: String) {
        orderDao.updateOrder(order.copy(status = newStatus))
    }

    suspend fun addFavorite(name: String) {
        favoriteDao.insertFavorite(Favorite(coffeeName = name))
    }

    suspend fun removeFavorite(favorite: Favorite) {
        favoriteDao.deleteFavorite(favorite)
    }
}
