package com.example.coffeeshop.di.presentation.ui.screens.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffeeshop.AppRepository
import com.example.coffeeshop.data.AppDatabase
import com.example.coffeeshop.data.Favorite
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: AppRepository
    val favorites: StateFlow<List<Favorite>>

    init {
        val database = AppDatabase.Companion.getInstance(application)
        repository = AppRepository(database.orderDao(), database.favoriteDao())
        favorites = repository.favorites.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.Lazily,
            initialValue = emptyList()
        )
    }

    fun toggleFavorite(name: String) {
        viewModelScope.launch {
            val current = favorites.value
            val existing = current.firstOrNull { it.coffeeName == name }
            if (existing != null) {
                repository.removeFavorite(existing)
            } else {
                repository.addFavorite(name)
            }
        }
    }
}