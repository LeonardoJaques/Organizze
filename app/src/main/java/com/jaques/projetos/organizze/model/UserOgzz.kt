package com.jaques.projetos.organizze.model


import com.jaques.projetos.organizze.helper.Base64Custom

import com.jaques.projetos.organizze.settings.SettingsFirebase


/** author Leonardo Jaques on 06/08/20 */

internal data class UserOgzz(
    internal var name: String = "",
    internal var email: String = "",
    internal var password: String = "",
    internal var totalExpenses: Double = 0.00,
    internal var totalRevenue: Double = 0.00

) {


    internal fun saveUserOgzz() {
        val user = SettingsFirebase.getFirebaseAuthOrganizze()
        val id = Base64Custom.codeBase64(user.currentUser!!.email.toString())

        val database = SettingsFirebase.getFirebaseRefenceOrganizze()
        val myRef = database.getReference("users")

        myRef.child(id).child("name").setValue(name)
        myRef.child(id).child("email").setValue(email)
        myRef.child(id).child("totalExpenses").setValue(totalRevenue)
        myRef.child(id).child("totalRevenue").setValue(totalRevenue)

    }

}

