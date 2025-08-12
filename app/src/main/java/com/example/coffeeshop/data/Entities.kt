package com.example.coffeeshop.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Representa un pedido realizado por el usuario
@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val coffeeName: String,
    val quantity: Int,
    val options: String,
    val status: String,            // "Pendiente", "Preparando", "Listo para recoger"
    val timestamp: Long = System.currentTimeMillis()
)

// Representa un café marcado como favorito
@Entity(tableName = "favorites")
data class Favorite(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val coffeeName: String
)
