package com.example.coffeeshop.data.repository

import android.util.Log
import com.example.coffeeshop.data.local.dao.OrderDao
import com.example.coffeeshop.data.local.entity.OrderEntity
import com.example.coffeeshop.data.model.Order
import com.example.coffeeshop.data.model.OrderStatus
import com.example.coffeeshop.domain.repository.IOrderRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Repositorio que sincroniza pedidos entre Room y Firestore
 * Colección en Firestore: "orders"
 */
class FirebaseOrderRepository(
    private val orderDao: OrderDao,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : IOrderRepository {

    private val ordersCollection = firestore.collection("orders")

    companion object {
        private const val TAG = "FirebaseOrderRepo"
    }

    /** 🔄 Lee pedidos del usuario en tiempo real (Firestore) */
    override fun getAllOrders(): Flow<List<Order>> = callbackFlow {
        val userId = auth.currentUser?.uid

        // ✅ IMPORTANTE: Verificar autenticación antes de consultar
        if (userId == null) {
            Log.w(TAG, "Usuario no autenticado, enviando lista vacía")
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val listener: ListenerRegistration = ordersCollection
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Error al escuchar pedidos: ${error.message}")
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                if (snapshot == null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                val orders = snapshot.documents.mapNotNull { doc ->
                    try {
                        val id = doc.getLong("id")?.toInt() ?: 0
                        val coffeeName = doc.getString("coffeeName") ?: ""
                        val quantity = doc.getLong("quantity")?.toInt() ?: 1
                        val options = doc.getString("options") ?: ""
                        val statusStr = doc.getString("status") ?: "PENDING"
                        val timestamp = doc.getLong("timestamp") ?: System.currentTimeMillis()

                        val status = try {
                            OrderStatus.valueOf(statusStr)
                        } catch (_: IllegalArgumentException) {
                            OrderStatus.PENDING
                        }

                        Order(
                            id = id,
                            coffeeName = coffeeName,
                            quantity = quantity,
                            options = options,
                            status = status,
                            timestamp = timestamp
                        )
                    } catch (e: Exception) {
                        Log.e(TAG, "Error parseando documento ${doc.id}: ${e.message}")
                        null
                    }
                }.sortedByDescending { it.timestamp }

                Log.d(TAG, "Pedidos cargados: ${orders.size}")
                trySend(orders)
            }

        awaitClose {
            Log.d(TAG, "Cerrando listener de pedidos")
            listener.remove()
        }
    }

    /** ➕ Insertar pedido en Room + Firestore */
    override suspend fun insertOrder(order: Order) {
        val userId = auth.currentUser?.uid

        if (userId == null) {
            Log.w(TAG, "No se puede insertar pedido: usuario no autenticado")
            return
        }

        // 1. Guardar local en Room
        try {
            orderDao.insertOrder(order.toEntity())
            Log.d(TAG, "✅ Pedido guardado en Room: ${order.coffeeName}")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error guardando en Room: ${e.message}")
        }

        // 2. Guardar remoto en Firestore
        val orderData = hashMapOf(
            "userId" to userId,
            "id" to order.id,
            "coffeeName" to order.coffeeName,
            "quantity" to order.quantity,
            "options" to order.options,
            "status" to order.status.name,
            "timestamp" to order.timestamp
        )

        try {
            val docRef = ordersCollection.add(orderData).await()
            Log.d(TAG, "✅ Pedido guardado en Firestore: ${docRef.id}")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error guardando en Firestore: ${e.message}")
        }
    }

    /** 🔁 Actualizar estado del pedido usando el campo numérico `id` */
    override suspend fun updateOrderStatus(orderId: Int, status: OrderStatus) {
        val userId = auth.currentUser?.uid

        if (userId == null) {
            Log.w(TAG, "No se puede actualizar: usuario no autenticado")
            return
        }

        Log.d(TAG, "Actualizando pedido $orderId a estado: $status")

        try {
            val querySnapshot = ordersCollection
                .whereEqualTo("userId", userId)
                .whereEqualTo("id", orderId)
                .get()
                .await()

            for (document in querySnapshot.documents) {
                document.reference.update("status", status.name).await()
                Log.d(TAG, "✅ Estado actualizado en Firestore")
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error actualizando estado: ${e.message}")
        }
    }

    /** 🗑 Eliminar pedido de Room + Firestore */
    override suspend fun deleteOrder(order: Order) {
        val userId = auth.currentUser?.uid

        if (userId == null) {
            Log.w(TAG, "No se puede eliminar: usuario no autenticado")
            return
        }

        // 1. Eliminar de Room
        try {
            orderDao.deleteOrder(order.toEntity())
            Log.d(TAG, "✅ Pedido eliminado de Room")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error eliminando de Room: ${e.message}")
        }

        // 2. Eliminar de Firestore
        try {
            val querySnapshot = ordersCollection
                .whereEqualTo("userId", userId)
                .whereEqualTo("id", order.id)
                .get()
                .await()

            for (document in querySnapshot.documents) {
                document.reference.delete().await()
                Log.d(TAG, "✅ Pedido eliminado de Firestore")
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error eliminando de Firestore: ${e.message}")
        }
    }

    /** 🧹 Limpiar pedidos locales (Room) */
    override suspend fun clearAllOrders() {
        try {
            orderDao.clearOrders()
            Log.d(TAG, "✅ Pedidos locales limpiados")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error limpiando pedidos: ${e.message}")
        }
    }

    // ==== Mappers ====
    private fun Order.toEntity() = OrderEntity(
        id = id,
        coffeeName = coffeeName,
        quantity = quantity,
        options = options,
        status = status.name,
        timestamp = timestamp
    )
}