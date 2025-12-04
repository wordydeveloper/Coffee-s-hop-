package com.example.coffeeshop.ui.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.coffeeshop.AppRoute
import com.example.coffeeshop.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import java.text.BreakIterator
import java.text.StringCharacterIterator

@Composable
fun splashScreen(navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()

    LaunchedEffect(true) {
        delay(2400)

        // ⚡ Verificar si el usuario está autenticado
        val currentUser = auth.currentUser

        if (currentUser != null) {
            // Usuario autenticado -> ir a HOME
            navController.navigate(AppRoute.HOME) {
                popUpTo(AppRoute.SPLASH) { inclusive = true }
            }
        } else {
            // Usuario NO autenticado -> ir a LOGIN
            navController.navigate(AppRoute.LOGIN) {
                popUpTo(AppRoute.SPLASH) { inclusive = true }
            }
        }
    }

    splash()
}

@Composable
fun splash() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.imagen),
            contentDescription = "imagen de entrada",
        )
        AnimatedText()
    }
}

@Composable
fun AnimatedText() {
    val texto = " Bienvenid@s ☕ "
    val breakIterator = remember(texto) { BreakIterator.getCharacterInstance() }
    val typeDelay = 80L
    var substringText by remember { mutableStateOf("") }

    LaunchedEffect(texto) {
        delay(700)
        breakIterator.text = StringCharacterIterator(texto)

        var nexIndex = breakIterator.next()
        while (nexIndex != BreakIterator.DONE) {
            substringText = texto.subSequence(0, nexIndex).toString()
            delay(typeDelay)
            nexIndex = breakIterator.next()
        }
    }

    Text(
        text = substringText,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold
    )
}