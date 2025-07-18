package com.example.coffeeshop.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.coffeeshop.R
import com.example.coffeeshop.navegation.AppScreen
import kotlinx.coroutines.delay

@Composable
fun splasScreen(navController: NavHostController){
    LaunchedEffect(key1 =true ) {
        delay(3000)
        navController.popBackStack()
        navController.navigate(AppScreen.MainScreen.route)
    }

splash()
}

@Composable
fun splash(){
Column(modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center)
{
Image(painter = painterResource(id = R.drawable.imagen),
    contentDescription = "imagendeentrada",
)
    Text("Bienvenid@s",
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold
    )

}
}


@Preview(showBackground = true)
@Composable
fun splasScreenPreview(){
splash()
}