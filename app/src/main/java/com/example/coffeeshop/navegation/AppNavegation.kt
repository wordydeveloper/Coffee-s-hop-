package com.example.coffeeshop.navegation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.coffeeshop.MainScreen
import com.example.coffeeshop.ui.theme.splasScreen

@Composable
fun AppNavegation(){
    val navController = rememberNavController()
    NavHost(navController = navController,
        startDestination = AppScreen.splasScren.route
        ){
        composable(AppScreen.splasScren.route){
            splasScreen(navController)
        }

        composable(AppScreen.MainScreen.route){
   MainScreen()
        }
    }
}