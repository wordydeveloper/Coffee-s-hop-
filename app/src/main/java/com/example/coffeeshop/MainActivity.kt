package com.example.coffeeshop
import androidx.compose.foundation.layout.Row
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.coffeeshop.navegation.AppNavegation
import com.example.coffeeshop.navegation.AppScreen
import com.example.coffeeshop.ui.theme.CoffeeshopTheme
import com.example.coffeeshop.ui.theme.splash
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoffeeshopTheme {
              AppNavegation()//llamo el splashScremm

                val navController = rememberNavController()
                val navigaAction = remember( navController) {
                    MyAppNavigationActions(navController)
                }
                val  navBackStackEnty by navController.currentBackStackEntryAsState()
                val selectedDestination = navBackStackEnty?.destination ?.route?:AppRoute.HOME

                
                MyAppconten(
                    modifier = Modifier,
                    navController = navController,
                    seletedDestination = selectedDestination,
                    navegateTopLevelDestination = navigaAction::navigateto
                )


                }
            }
        }

    }


@Composable
fun MyAppconten(
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
                startDestination = AppRoute.HOME
            ) {
                composable(AppRoute.HOME) {

                }
                composable(AppRoute.SEARCH) {

                }
                composable(AppRoute.SETTINGS) {

                }
                composable(AppRoute.FAVORITE) {

                }
                composable(AppRoute.PROFILE) {

                }

            }
            ComposePreview(seletedDestination=seletedDestination,
                navegateTopLevelDestination=navegateTopLevelDestination
            )
        }
    }
}


@Composable
fun ComposePreview(seletedDestination:String,
                   navegateTopLevelDestination:(AppToplevel) ->Unit
) {
NavigationBar (modifier = Modifier.fillMaxWidth()){
    TopLevelDestination.forEach { destinations ->
NavigationBarItem(
    selected = seletedDestination == destinations.route,
    onClick = { navegateTopLevelDestination(destinations) },
    icon ={
        Icon(
            imageVector = destinations.selecIcon,
            contentDescription = stringResource(id = destinations.IconText)
        )
            }
          )
       }
    }
}