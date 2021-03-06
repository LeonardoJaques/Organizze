package com.jaques.projetos.organizze.settings


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.jaques.projetos.organizze.helper.Base64Custom

/** author Leonardo Jaques on 06/08/20 */
class SettingsFirebase {

    companion object {
        private var auth: FirebaseAuth = FirebaseAuth.getInstance()
        fun getFirebaseAuthOrganizze(): FirebaseAuth {
            auth = Firebase.auth
            return auth
        }

        fun getFirebaseRefenceOrganizze(): FirebaseDatabase = Firebase.database
        fun id() = Base64Custom.codeBase64(auth.currentUser!!.email.toString())

        fun movPath() = getFirebaseRefenceOrganizze().getReference("movement")
        fun userPath() = getFirebaseRefenceOrganizze().getReference("users")


        fun fireBaseRef() = getFirebaseRefenceOrganizze().reference

    }


}
