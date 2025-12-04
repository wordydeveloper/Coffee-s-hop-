package com.example.coffeeshop.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging

/**
 * Singleton para acceder a instancias de Firebase
 */
object FirebaseConfig {

    val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    val firestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    val messaging: FirebaseMessaging by lazy {
        FirebaseMessaging.getInstance()
    }

    // Colecciones de Firestore
    object Collections {
        const val USERS = "users"
        const val ORDERS = "orders"
        const val FAVORITES = "favorites"
        const val PRODUCTS = "products"
    }
}