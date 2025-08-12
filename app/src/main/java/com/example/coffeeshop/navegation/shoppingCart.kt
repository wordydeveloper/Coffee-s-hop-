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
import com.example.coffeeshop.ui.theme.Coffee
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
// Pantalla del carrito + pedidos
// -----------------------------
@Composable
fun CartScreen() {
    val vm: OrderViewModel = viewModel()

    // Carrito (memoria)
    val cartItems = ShoppingCart.items
    val total = ShoppingCart.getTotal()

    // Pedidos desde Room (flujo del ViewModel)
    val orders by vm.orders.collectAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        // ===== Carrito =====
        Text("Carrito", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))

        if (cartItems.isEmpty()) {
            Text("Tu carrito está vacío.")
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(cartItems) { coffee ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(coffee.name, style = MaterialTheme.typography.titleMedium)
                            Text(coffee.price)
                        }
                        Button(onClick = { ShoppingCart.remove(coffee) }) {
                            Text("Quitar")
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total: $${String.format("%.2f", total)}")
                Button(onClick = {
                    cartItems.forEach { coffee ->
                        vm.placeOrder(coffee.name, 1, "")
                    }
                    ShoppingCart.clear()
                }) { Text("Realizar pedido") }
            }
        }

        Spacer(Modifier.height(24.dp))
        Divider()
        Spacer(Modifier.height(12.dp))

        // ===== Pedidos (Room) =====
        Text("Mis pedidos", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))

        if (orders.isEmpty()) {
            Text("Aún no tienes pedidos.")
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
            ) {
                items(orders, key = { it.id }) { order ->
                    ElevatedCard(Modifier.fillMaxWidth()) {
                        Column(Modifier.fillMaxWidth().padding(12.dp)) {
                            Text(order.coffeeName, style = MaterialTheme.typography.titleMedium)
                            Text("Cantidad: ${order.quantity}")
                            if (order.options.isNotBlank()) Text("Opciones: ${order.options}")
                            Spacer(Modifier.height(6.dp))
                            Text("Estado: ${order.status}")

                            Spacer(Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(onClick = { vm.advanceStatus(order) }) { Text("Avanzar estado") }
                                OutlinedButton(onClick = { vm.setStatus(order, "Pendiente") }) { Text("Pendiente") }
                                OutlinedButton(onClick = { vm.setStatus(order, "Preparando") }) { Text("Preparando") }
                                OutlinedButton(onClick = { vm.setStatus(order, "Listo para recoger") }) { Text("Listo") }
                            }
                        }
                    }
                }
            }
        }
    }
}
