package com.jaques.projetos.organizze.helper

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

private val currencyFormat = DecimalFormat(
    "#,##0.00",
    DecimalFormatSymbols(Locale("pt", "BR"))
)

fun Double.toBRL(): String = "R$ ${currencyFormat.format(this)}"
