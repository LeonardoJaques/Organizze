package com.jaques.projetos.organizze.helper

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


/** author Leonardo Jaques on 12/08/20 */
class DateCustom {

    companion object {


        fun dateCurrent(): String {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            return current.format(formatter)
        }

        fun dateChoose(date: String): String {
            val returnDate = date.split("/")
            val day = returnDate[0]
            val month = returnDate[1]
            val year = returnDate[2]
            return month + year
        }
    }

}