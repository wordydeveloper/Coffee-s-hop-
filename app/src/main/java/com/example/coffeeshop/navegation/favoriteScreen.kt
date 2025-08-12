package com.example.coffeeshop.navegation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.coffeeshop.viewmodel.FavoriteViewModel

@Composable
fun favoriteScreen() {
    val vm: FavoriteViewModel = viewModel()
    val favorites by vm.favorites.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Favoritos", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))

        if (favorites.isEmpty()) {
            Text("Aún no tienes cafés favoritos.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(favorites, key = { it.id }) { fav ->
                    ElevatedCard(Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(fav.coffeeName, style = MaterialTheme.typography.titleMedium)
                            TextButton(onClick = { vm.toggleFavorite(fav.coffeeName) }) {
                                Text("Eliminar")
                            }
                        }
                    }
                }
            }
        }
    }
}
