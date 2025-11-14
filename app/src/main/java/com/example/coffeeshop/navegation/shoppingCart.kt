package com.example.coffeeshop.navegation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.coffeeshop.ui.ui.Coffee
import com.example.coffeeshop.viewmodel.OrderViewModel

// -----------------------------
// Carrito en memoria (reactivo)
// -----------------------------
object ShoppingCart {
    private val _items = mutableStateListOf<Coffee>()
    val items: List<Coffee> get() = _items

    fun add(coffee: Coffee) { _items.add(coffee) }
    fun remove(coffee: Coffee) { _items.remove(coffee) }
    fun clear() { _items.clear() }

    fun getTotal(): Double =
        _items.sumOf { it.price.removePrefix("$").toDoubleOrNull() ?: 0.0 }
}

// -----------------------------

