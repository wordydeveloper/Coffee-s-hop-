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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.coffeeshop.navegation.CartScreen
import com.example.coffeeshop.navegation.SearchScreen
import com.example.coffeeshop.navegation.favoriteScreen
import com.example.coffeeshop.navegation.profileScreen
import com.example.coffeeshop.navegation.ShoppingCart
import com.example.coffeeshop.ui.theme.CoffeeshopTheme
import com.example.coffeeshop.ui.theme.splashScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoffeeshopTheme {
                val navController = rememberNavController()
                val navigationActions = remember(navController) {
                    MyAppNavigationActions(navController)
                }
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val selectedDestination = navBackStackEntry?.destination?.route ?: AppRoute.HOME

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
    Row(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            NavHost(
                modifier = Modifier.weight(1f),
                navController = navController,
                startDestination = AppRoute.SPLASH
            ) {
                // SplashScreen
                composable(AppRoute.SPLASH) {
                    splashScreen(navController)
                }

                composable(AppRoute.HOME) {
                    MainScreen()
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

            // Barra inferior de navegación
            if (seletedDestination != AppRoute.SPLASH) {
                ComposePreview(
                    seletedDestination = seletedDestination,
                    navegateTopLevelDestination = navegateTopLevelDestination
                )
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

