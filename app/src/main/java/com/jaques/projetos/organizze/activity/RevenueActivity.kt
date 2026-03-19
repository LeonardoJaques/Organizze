package com.jaques.projetos.organizze.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.jaques.projetos.organizze.R
import com.jaques.projetos.organizze.databinding.ActivityRevenueBinding
import com.jaques.projetos.organizze.helper.DateCustom
import com.jaques.projetos.organizze.model.Movement
import com.jaques.projetos.organizze.settings.SettingsFirebase

class RevenueActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRevenueBinding

    private var totalRevenue: Double = 0.00


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRevenueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.editViewDateRevenue.setText(DateCustom.dateCurrent())
        getRevenueTotal()

    }


    fun saveRevenue(view: View) {
        if (validateFieldsRevenue()) {
            val date: String = binding.editViewDateRevenue.text.toString()
            val value = binding.editViewRevenueValue.text.toString().toDouble()
            val category = binding.editViewEntryR.text.toString()
            val description = binding.editViewDescriptionR.text.toString()
            val type = "r"

            save(date, category, description, type, value)
        } else validateFieldsRevenue()
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
        val id = SettingsFirebase.id()
        return SettingsFirebase.fireBaseRef().child("users")
            .child(id)
            .child("totalRevenue")
    }

    private fun save(
        date: String?,
        category: String,
        description: String,
        type: String,
        value: Double
    ) {
        val monthYear: String? = date?.let { DateCustom.dateChoose(it) }

        val firebase: DatabaseReference = SettingsFirebase.movPath()
        val userId = SettingsFirebase.id()
        if (monthYear != null) {
            val firebaseAttributes = firebase.child(userId).child(monthYear).push()
            firebaseAttributes.child("date").setValue(date)
            firebaseAttributes.child("category").setValue(category)
            firebaseAttributes.child("description").setValue(description)
            firebaseAttributes.child("type").setValue(type)
            firebaseAttributes.child("value").setValue(value)

            val updateValue = value + totalRevenue
            userData().setValue(updateValue)
            finish()
        }
    }

    private fun validateFieldsRevenue(): Boolean {
        when {
            binding.editViewEntryR.text!!.isEmpty() -> Toast.makeText(
                this,
                "Preecha a Categoria",
                Toast.LENGTH_LONG
            ).show()
            binding.editViewDescriptionR.text!!.isEmpty() -> Toast.makeText(
                this,
                "Preecha a Descrição",
                Toast.LENGTH_LONG
            ).show()
            binding.editViewRevenueValue.text.toString().isEmpty() -> Toast.makeText(
                this,
                "Preecha o valor",
                Toast.LENGTH_LONG
            ).show()
            binding.editViewDateRevenue.text.toString().isEmpty() -> binding.editViewDateRevenue.setText(DateCustom.dateCurrent())
            else -> return true
        }

        return false
    }

}