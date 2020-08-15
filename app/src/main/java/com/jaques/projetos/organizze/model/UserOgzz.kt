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
        val sequence = myRef.child(id)

        sequence.child("name").setValue(name)
        sequence.child("email").setValue(email)
        sequence.child("totalExpenses").setValue(totalRevenue)
        sequence.child("totalRevenue").setValue(totalRevenue)

    }

}

