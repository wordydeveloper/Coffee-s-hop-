package com.example.coffeeshop.domain.repository

import kotlinx.coroutines.flow.Flow

interface IFavoriteRepository {
    fun getAllFavorites(): Flow<List<String>>
    suspend fun addFavorite(coffeeName: String)
    suspend fun removeFavorite(coffeeName: String)
    suspend fun isFavorite(coffeeName: String): Boolean
}