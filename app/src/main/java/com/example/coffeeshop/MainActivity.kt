package com.example.coffeeshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.coffeeshop.di.AppModule
import com.example.coffeeshop.di.presentation.ui.screens.auth.SignupScreen
import com.example.coffeeshop.di.presentation.ui.screens.cart.CartScreen
import com.example.coffeeshop.di.presentation.ui.screens.search.SearchScreen
import com.example.coffeeshop.navegation.favoriteScreen
import com.example.coffeeshop.navegation.profileScreen
import com.example.coffeeshop.ui.screens.auth.LoginScreen
import com.example.coffeeshop.ui.ui.CoffeeshopTheme
import com.example.coffeeshop.ui.ui.splashScreen
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ❌ Siempre cerrar sesión al iniciar la app
        FirebaseAuth.getInstance().signOut()

        // ⚡ Inicializar el módulo de dependencias (Room, etc.)
        AppModule.initializeDatabase(applicationContext)

        enableEdgeToEdge()
        setContent {
            CoffeeshopTheme {
                val navController = rememberNavController()
                val navigationActions = MyAppNavigationActions(navController)
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val selectedDestination =
                    navBackStackEntry?.destination?.route ?: AppRoute.SPLASH

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
    // Rutas donde NO se muestra la BottomBar
    val noBottomBarRoutes = listOf(
        AppRoute.SPLASH,
        AppRoute.LOGIN,
        AppRoute.SIGNUP
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            if (seletedDestination !in noBottomBarRoutes) {
                ComposeBottomBar(
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
fun ComposeBottomBar(
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
