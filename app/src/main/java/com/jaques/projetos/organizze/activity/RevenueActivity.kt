package com.jaques.projetos.organizze.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.jaques.projetos.organizze.R
import com.jaques.projetos.organizze.helper.Base64Custom
import com.jaques.projetos.organizze.helper.DateCustom
import com.jaques.projetos.organizze.model.Movement
import com.jaques.projetos.organizze.settings.SettingsFirebase
import kotlinx.android.synthetic.main.activity_revenue.*

class RevenueActivity : AppCompatActivity() {

    private lateinit var fieldDate: TextInputEditText
    private lateinit var fieldCategory: TextInputEditText
    private lateinit var fieldDescription: TextInputEditText
    private lateinit var fieldValue: EditText

    private var totalRevenue: Double = 0.00

    private lateinit var floatingActionButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_revenue)

        fieldValue = editViewRevenueValue
        fieldDate = editViewDateRevenue
        fieldCategory = editViewEntryR
        fieldDescription = editViewDescriptionR
        floatingActionButton = fabSaveRevenueView


        fieldDate.setText(DateCustom.dateCurrent())
        getRevenueTotal()

        floatingActionButton.setOnClickListener {
            validateFieldsRevenue()

        }

    }

    private fun validateFieldsRevenue() {
        when {
            fieldCategory.text!!.isEmpty() -> Toast.makeText(
                this,
                "Preecha a Categoria",
                Toast.LENGTH_LONG
            ).show()
            fieldDescription.text!!.isEmpty() -> Toast.makeText(
                this,
                "Preecha a Descrição",
                Toast.LENGTH_LONG
            ).show()
            fieldValue.text.toString().isEmpty() -> Toast.makeText(
                this,
                "Preecha o valor",
                Toast.LENGTH_LONG
            ).show()
            fieldDate.text.toString().isEmpty() -> fieldDate.setText(DateCustom.dateCurrent())

            else -> saveRevenue()
        }
    }

    private fun saveRevenue() {
        val movement = Movement()
        val category = fieldCategory.text
        val description = fieldDescription.text
        val value = fieldValue.text.toString().toDouble()
        val date = fieldDate.text.toString()
        val type = "r"

        val updateValue = value + totalRevenue
        userData().setValue(updateValue)
        movement.saveMovementOgzz(category, description, value, date, type)
        finish()
    }

    private fun getRevenueTotal(): Double {
        userData().addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val expenseDb = snapshot.value
                totalRevenue = expenseDb.toString().toDouble()
            }
        })
        return totalRevenue
    }

    private fun userData(): DatabaseReference {
        val database = SettingsFirebase
            .getFirebaseRefenceOrganizze()
            .reference
        val auth: FirebaseAuth = SettingsFirebase
            .getFirebaseAuthOrganizze()

        val id = Base64Custom.codeBase64(
            auth.currentUser!!
                .email
                .toString()
        )
        return database.child("users")
            .child(id)
            .child("totalRevenue")
    }


}