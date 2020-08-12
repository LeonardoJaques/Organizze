package com.jaques.projetos.organizze.helper

import android.os.Build
import android.util.Base64
import androidx.annotation.RequiresApi


/** author Leonardo Jaques on 11/08/20 */
class Base64Custom {
    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun codeBase64(text: String): String =
            java.util.Base64
                .getEncoder()
                .withoutPadding()
                .encodeToString(text.toByteArray())

        @RequiresApi(Build.VERSION_CODES.O)
        fun decodeBase64(textCode: String): String =
            java.util.Base64
                .getDecoder()
                .decode(textCode.toByteArray())
                .toString()


    }

}