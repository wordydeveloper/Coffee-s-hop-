package com.example.coffeeshop.navegation

import com.example.coffeeshop.Coffee
import com.example.coffeeshop.R

fun CoffeeData(){


    val coffeeList = listOf(
        Coffee(R.drawable.i1, "Expreso", "Café intenso con cuerpo robusto y sabor profundo.", "$2.50"),
        Coffee(R.drawable.i2, "Americano", "Café ligero preparado con agua caliente sobre el expreso.", "$2.00"),
        Coffee(R.drawable.i3, "Crema", "Café con una capa suave de crema espumosa.", "$3.00"),
        Coffee(R.drawable.ii5, "Negro", "Café puro sin aditivos, ideal para los que buscan autenticidad.", "$1.80"),
        Coffee(R.drawable.i6, "Americano", "Variante del americano con un toque más tostado.", "$2.10"),
        Coffee(R.drawable.ii7, "Frío", "Café helado refrescante para días cálidos.", "$3.20")
    )

}