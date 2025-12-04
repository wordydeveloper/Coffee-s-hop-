package com.example.coffeeshop.navegation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.coffeeshop.viewmodel.OrderViewModel

@Composable
fun profileScreen(
    navController: NavHostController   // ✅ ahora recibe navController
) {
    val orderViewModel: OrderViewModel = viewModel()
    val orders by orderViewModel.orders.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Mi perfil",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(8.dp))

        if (orders.isEmpty()) {
            Text("No has realizado pedidos todavía.")
        } else {
            LazyColumn {
                items(orders) { order ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        colors = CardDefaults.elevatedCardColors()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                "${order.coffeeName} x${order.quantity}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                "Opciones: ${order.options}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                "Estado: ${order.status}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}
