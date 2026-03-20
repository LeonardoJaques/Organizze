package com.jaques.projetos.organizze.helper

import java.text.SimpleDateFormat
import java.util.*

object DateCustom {

    private val brLocale = Locale("pt", "BR")

    fun dateCurrent(): String =
        SimpleDateFormat("dd/MM/yyyy", brLocale).format(Date())

    fun dateChoose(date: String): String {
        val parts = date.split("/")
        return parts[1] + parts[2]
    }
}
