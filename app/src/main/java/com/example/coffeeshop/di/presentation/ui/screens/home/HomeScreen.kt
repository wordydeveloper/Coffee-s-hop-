package com.example.coffeeshop.di.presentation.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    val viewModel: HomeViewModel = viewModel(
      //  factory = ViewModelFactory()
    )

    // ... resto del código
}