package com.example.coffeeshop.data.repository

import com.example.coffeeshop.data.local.dao.FavoriteDao
import com.example.coffeeshop.data.local.entity.FavoriteEntity
import com.example.coffeeshop.domain.repository.IFavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteRepository(
    private val favoriteDao: FavoriteDao
) : IFavoriteRepository {

    override fun getAllFavorites(): Flow<List<String>> {
        return favoriteDao.getAllFavorites().map { entities ->
            entities.map { it.coffeeName }
        }
    }

    override suspend fun addFavorite(coffeeName: String) {
        favoriteDao.insertFavorite(FavoriteEntity(coffeeName = coffeeName))
    }

    override suspend fun removeFavorite(coffeeName: String) {
        val favorite = favoriteDao.getFavoriteByName(coffeeName)
        favorite?.let { favoriteDao.deleteFavorite(it) }
    }

    override suspend fun isFavorite(coffeeName: String): Boolean {
        return favoriteDao.getFavoriteByName(coffeeName) != null
    }
}
