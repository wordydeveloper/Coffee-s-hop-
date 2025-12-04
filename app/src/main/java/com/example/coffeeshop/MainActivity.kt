package com.example.coffeeshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.coffeeshop.di.AppModule
import com.example.coffeeshop.di.presentation.ui.screens.auth.SignupScreen
import com.example.coffeeshop.di.presentation.ui.screens.cart.CartScreen
import com.example.coffeeshop.di.presentation.ui.screens.search.SearchScreen
import com.example.coffeeshop.navegation.favoriteScreen
import com.example.coffeeshop.navegation.profileScreen
import com.example.coffeeshop.ui.screens.auth.LoginScreen

import com.example.coffeeshop.ui.ui.CoffeeshopTheme
import com.example.coffeeshop.ui.ui.splashScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ⚡ IMPORTANTE: Inicializar el módulo de dependencias
        AppModule.initializeDatabase(applicationContext)

        enableEdgeToEdge()
        setContent {
            CoffeeshopTheme {
                val navController = rememberNavController()
                val navigationActions = remember(navController) {
                    MyAppNavigationActions(navController)
                }
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val selectedDestination = navBackStackEntry?.destination?.route ?: AppRoute.SPLASH

                MyAppContent(
                    modifier = Modifier,
                    navController = navController,
                    seletedDestination = selectedDestination,
                    navegateTopLevelDestination = navigationActions::navigateto
                )
            }
        }
    }
}

@Composable
fun MyAppContent(
    modifier: Modifier,
    navController: NavHostController,
    seletedDestination: String,
    navegateTopLevelDestination: (AppToplevel) -> Unit
) {
    // Lista de rutas donde NO se muestra el BottomBar
    val noBottomBarRoutes = listOf(
        AppRoute.SPLASH,
        AppRoute.LOGIN,
        AppRoute.SIGNUP
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            if (seletedDestination !in noBottomBarRoutes) {
                ComposePreview(
                    seletedDestination = seletedDestination,
                    navegateTopLevelDestination = navegateTopLevelDestination
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            navController = navController,
            startDestination = AppRoute.SPLASH
        ) {
            composable(AppRoute.SPLASH) {
                splashScreen(navController)
            }
            composable(AppRoute.LOGIN) {
                LoginScreen(navController)
            }
            composable(AppRoute.SIGNUP) {
                SignupScreen(navController)
            }
            composable(AppRoute.HOME) {
                MainScreen(navController = navController)
            }
            composable(AppRoute.SEARCH) {
                SearchScreen(navController = navController)
            }
            composable(AppRoute.SHOPPINGCART) {
                CartScreen()
            }
            composable(AppRoute.FAVORITE) {
                favoriteScreen()
            }
            composable(AppRoute.PROFILE) {
                profileScreen()
            }
        }
    }
}

@Composable
fun ComposePreview(
    seletedDestination: String,
    navegateTopLevelDestination: (AppToplevel) -> Unit
) {
    NavigationBar(modifier = Modifier.fillMaxWidth()) {
        TopLevelDestination.forEach { destination ->
            NavigationBarItem(
                selected = seletedDestination == destination.route,
                onClick = { navegateTopLevelDestination(destination) },
                icon = {
                    Icon(
                        imageVector = destination.selecIcon,
                        contentDescription = stringResource(id = destination.IconText)
                    )
                }
            )
        }
    }
}