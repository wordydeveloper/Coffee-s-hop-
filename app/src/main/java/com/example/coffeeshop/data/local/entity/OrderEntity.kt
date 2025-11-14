package com.example.coffeeshop.data.local.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val coffeeName: String,
    val quantity: Int,
    val options: String,
    val status: String,
    val timestamp: Long = System.currentTimeMillis()
)