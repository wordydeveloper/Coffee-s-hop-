package com.example.coffeeshop

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.coffeeshop.ui.ui.Coffee
import com.example.coffeeshop.ui.ui.coffeeList
import com.example.coffeeshop.viewmodel.OrderViewModel

@Composable
fun MainScreen(navController: NavController) {
    val orderViewModel: OrderViewModel = viewModel()
    var selectedCoffee by remember { mutableStateOf<Coffee?>(null) }

    // Lista de cafés
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        items(coffeeList) { coffee ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedCoffee = coffee }
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = coffee.imageRes),
                    contentDescription = coffee.name,
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(coffee.name, style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(2.dp))
                    Text(coffee.description, maxLines = 2, style = MaterialTheme.typography.bodySmall)
                    Spacer(Modifier.height(2.dp))
                    Text("Precio: ${coffee.price}", style = MaterialTheme.typography.bodyMedium)
                }
                Spacer(Modifier.width(8.dp))
                Button(onClick = { selectedCoffee = coffee }) {
                    Text("Pedir")
                }
            }
            Divider()
        }
    }

    // Diálogo de pedido con imagen visible
    selectedCoffee?.let { coffee ->
        var quantity by remember { mutableStateOf(1) }
        var addSugar by remember { mutableStateOf(false) }
        var addMilk by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { selectedCoffee = null },
            title = { Text(coffee.name) },
            text = {
                Column {
                    // Imagen ARRIBA (no dentro del Row) para que no se colapse
                    Image(
                        painter = painterResource(id = coffee.imageRes),
                        contentDescription = coffee.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(Modifier.height(8.dp))
                    Text("Descripción: ${coffee.description}")
                    Spacer(Modifier.height(8.dp))
                    Text("Precio: ${coffee.price}")
                    Spacer(Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Cantidad:")
                        Spacer(Modifier.width(8.dp))
                        IconButton(onClick = { if (quantity > 1) quantity-- }) {
                            Icon(Icons.Filled.Remove, contentDescription = "Restar")
                        }
                        Text(quantity.toString())
                        IconButton(onClick = { quantity++ }) {
                            Icon(Icons.Filled.Add, contentDescription = "Sumar")
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = addSugar, onCheckedChange = { addSugar = it })
                        Text("Agregar azúcar")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = addMilk, onCheckedChange = { addMilk = it })
                        Text("Agregar leche")
                    }
                }
            },
            confirmButton = {
                Column(horizontalAlignment = Alignment.End) {
                    Button(onClick = {
                        val options = buildString {
                            append("Cantidad: $quantity")
                            if (addSugar) append(", con azúcar")
                            if (addMilk) append(", con leche")
                        }
                        orderViewModel.placeOrder(coffee.name, quantity, options)
                        selectedCoffee = null
                    }) {
                        Text("Realizar pedido")
                    }
                    Spacer(Modifier.height(4.dp))
                    TextButton(onClick = { selectedCoffee = null }) {
                        Text("Cancelar")
                    }
                }
            }
        )
    }
}
