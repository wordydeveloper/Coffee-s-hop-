package com.example.coffeeshop.domain.usecase.favorite

import com.example.coffeeshop.domain.repository.IFavoriteRepository
import kotlinx.coroutines.flow.Flow

class GetAllFavoritesUseCase(
    private val favoriteRepository: IFavoriteRepository
) {
    operator fun invoke(): Flow<List<String>> {
        return favoriteRepository.getAllFavorites()
    }
}