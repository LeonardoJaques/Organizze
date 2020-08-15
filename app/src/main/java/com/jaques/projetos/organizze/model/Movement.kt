package com.jaques.projetos.organizze.model

import android.text.Editable
import com.jaques.projetos.organizze.helper.Base64Custom
import com.jaques.projetos.organizze.settings.SettingsFirebase


/** author Leonardo Jaques on 12/08/20 */
data class Movement(
    internal var date: String = "",
    internal var category: String = "",
    internal var description: String = "",
    internal var type: String = "",
    internal var value: Double = 0.00
) {
    fun saveMovementOgzz(
        category: Editable?,
        description: Editable?,
        value: Double,
        date: Editable?
    ) {

        val auth = SettingsFirebase.getFirebaseAuthOrganizze()
        val id = Base64Custom.codeBase64(auth.currentUser!!.email.toString())
        val database = SettingsFirebase.getFirebaseRefenceOrganizze()

        val myRef = database.getReference("movement")
        val sequence = myRef.child(id).push()
        sequence.child("value").setValue(value)
        sequence.child("date").setValue(date.toString())
        sequence.child("category").setValue(category.toString())
        sequence.child("description").setValue(description.toString())

    }
}

