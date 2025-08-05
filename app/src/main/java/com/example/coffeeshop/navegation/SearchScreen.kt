package com.example.coffeeshop.navegation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coffeeshop.R

data class Coffee(val imageRes: Int, val name: String, val description: String)

@Composable
fun SearchScreen() {
    val coffeeList = listOf(
        Coffee(R.drawable.i1, "Expreso", "Café concentrado con sabor intenso."),
        Coffee(R.drawable.i2, "Americano", "Café negro diluido en agua caliente."),
        Coffee(R.drawable.i3, "Crema", "Café suave con textura espumosa."),
        Coffee(R.drawable.ii5, "Negro", "El clásico café sin aditivos."),
        Coffee(R.drawable.i6, "Americano", "Otra variante americana sin leche."),
        Coffee(R.drawable.ii7, "Frío", "Café frío ideal para días calurosos.")
    )

    var searchQuery by remember { mutableStateOf("") }

    val matchedItem = coffeeList.firstOrNull {
        it.name.contains(searchQuery.trim(), ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Buscar café",
            fontSize = 32.sp,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
        )

        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Ej: Americano") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (searchQuery.isEmpty()) {
            Text(
                "Ingresa el nombre de un café para buscarlo.",
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            matchedItem?.let { coffee ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = coffee.imageRes),
                        contentDescription = coffee.name,
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = coffee.name,
                        fontSize = 24.sp,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = coffee.description,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            } ?: Text(
                text = "No se encontró un café con ese nombre ☕",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}