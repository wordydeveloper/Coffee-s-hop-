package com.example.coffeeshop.navegation

sealed class AppScreen(val route:String) {
    object splasScren:AppScreen("splash_Screen")
    object MainScreen:AppScreen("main_Screen")

}