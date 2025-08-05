package com.example.coffeeshop

import androidx.compose.foundation.MutatePriority
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController



class MyAppNavigationActions(private val navController: NavHostController){
    fun navigateto(destination: AppToplevel){
        navController.navigate(destination.route){
            popUpTo(navController.graph.findStartDestination().id){
                saveState =true
        }
        launchSingleTop= true
        }
    }
}
data class AppToplevel (
    val route:String,
    val selecIcon : ImageVector,
    val IconText:Int
    )

val TopLevelDestination = listOf(
    AppToplevel(
        route =AppRoute.HOME ,
        selecIcon = Icons.Default.Home,
        IconText =R.string.home
    ),
    AppToplevel(
        route =AppRoute.SEARCH ,
        selecIcon = Icons.Default.Search,
        IconText =R.string.search
    ),
    AppToplevel(
        route =AppRoute.SETTINGS ,
        selecIcon = Icons.Default.ShoppingCart,
        IconText =R.string.settings
    ),
    AppToplevel(
        route =AppRoute.FAVORITE ,
        selecIcon = Icons.Default.Favorite,
        IconText =R.string.favorite
    ),
    AppToplevel(
        route =AppRoute.PROFILE ,
        selecIcon = Icons.Default.Person,
        IconText =R.string.profile
    ),

    )
object AppRoute{
     const val HOME ="home"
    const val SEARCH ="search"
    const val SETTINGS =" settings"
    const val PROFILE =" profile"
    const val FAVORITE =" favorite"

}