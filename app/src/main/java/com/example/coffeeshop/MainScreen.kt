package com.example.coffeeshop

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coffeeshop.ui.theme.Coffee
import com.example.coffeeshop.ui.theme.coffeeList


@Composable
fun CoffeeItem(
    coffee: Coffee,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(coffee.imageRes),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(160.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
    )
}

@Composable
fun MainScreen() {
    var selectedCoffee by remember { mutableStateOf<Coffee?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "Coffees",
                fontSize = 45.sp,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        items(coffeeList.chunked(2)) { row ->
            Row(modifier = Modifier.fillMaxWidth()) {
                row.forEach { coffee ->
                    CoffeeItem(
                        coffee = coffee,
                        onClick = { selectedCoffee = coffee },
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    )
                }
                if (row.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }

    selectedCoffee?.let { coffee ->
        AlertDialog(
            onDismissRequest = { selectedCoffee = null },
            confirmButton = {
                TextButton(onClick = { selectedCoffee = null }) {
                    Text("Cerrar")
                }
            },
            title = { Text(coffee.name) },
            text = {
                Column {
                    Text("Descripción: ${coffee.description}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Precio: ${coffee.price}")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    MainScreen()
}