package com.example.coffeeshop.domain.usecase.favorite

import com.example.coffeeshop.domain.repository.IFavoriteRepository

class ToggleFavoriteUseCase(
    private val favoriteRepository: IFavoriteRepository
) {
    suspend operator fun invoke(coffeeName: String) {
        if (favoriteRepository.isFavorite(coffeeName)) {
            favoriteRepository.removeFavorite(coffeeName)
        } else {
            favoriteRepository.addFavorite(coffeeName)
        }
    }
}