package com.example.coffeeshop.navegation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.coffeeshop.ui.theme.Coffee

object ShoppingCart {
    private val items = mutableListOf<Coffee>()

    fun add(coffee: Coffee) {
        items.add(coffee)
    }

    fun remove(coffee: Coffee) {
        items.remove(coffee)
    }

    fun clear() {
        items.clear()
    }

    fun getItems(): List<Coffee> = items.toList()

    fun getTotal(): Double =
        items.sumOf { it.price.removePrefix("$").toDoubleOrNull() ?: 0.0 }
}
@Composable
fun CartScreen() {
    val items = ShoppingCart.getItems()
    val total = ShoppingCart.getTotal()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Carrito de compras", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(8.dp))

        items.forEach { coffee ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(coffee.name)
                    Text(coffee.price)
                }
                Button(onClick = { ShoppingCart.remove(coffee) }) {
                    Text("Quitar")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Total: $${String.format("%.2f", total)}")
        Button(onClick = { ShoppingCart.clear() }) {
            Text("Vaciar carrito")
        }
    }
}
