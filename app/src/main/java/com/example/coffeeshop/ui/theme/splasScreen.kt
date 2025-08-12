package com.example.coffeeshop.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.coffeeshop.AppRoute
import com.example.coffeeshop.R

import kotlinx.coroutines.delay
import java.text.BreakIterator
import java.text.StringCharacterIterator

@Composable
fun splashScreen(navController: NavHostController){
    LaunchedEffect(true) {
        delay(2400)
        navController.navigate(AppRoute.HOME) {
            popUpTo(AppRoute.SPLASH) { inclusive = true }
        }
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
    AnimatedText()
   /* Text("Bienvenid@s",
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold
    )
    */

}
}


@Preview(showBackground = true)
@Composable
fun splasScreenPreview(){
splash()
}


@Composable

fun AnimatedText() {
  val texto =" Bienvenid@s ☕ "
    val breakIterator = remember (texto){BreakIterator.getCharacterInstance()  }
    val typeDelay =  80L
     var substringText  by remember { mutableStateOf("") }

    LaunchedEffect (texto){
        delay(700)
        breakIterator.text =StringCharacterIterator(texto)


        var nexIndex = breakIterator.next()
        while (nexIndex != BreakIterator.DONE){
            substringText = texto.subSequence(0, nexIndex).toString()
            delay(typeDelay)
            nexIndex =breakIterator.next()

        }
    }
    Text(

        text = substringText,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold
    )
}