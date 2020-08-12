package com.jaques.projetos.organizze.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jaques.projetos.organizze.helper.Base64Custom
import com.jaques.projetos.organizze.settings.SettingsFirebase

/** author Leonardo Jaques on 06/08/20 */
data class UserOgzz(
    internal var name: String = "",
    internal var email: String = "",
    internal var password: String = ""
) {

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveUserOgzz() {
        val userOgzzId = Base64Custom.codeBase64(text = this.email)
        val database = SettingsFirebase.getFirebaseRefenceOrganizze()
        val myRef = database.getReference("users")
        myRef.child(userOgzzId).child("name").setValue(name)
        myRef.child(userOgzzId).child("email").setValue(email)
    }

}