package com.jaques.projetos.organizze.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.jaques.projetos.organizze.R
import com.jaques.projetos.organizze.databinding.ActivityExpenseBinding
import com.jaques.projetos.organizze.helper.DateCustom
import com.jaques.projetos.organizze.model.Movement
import com.jaques.projetos.organizze.settings.SettingsFirebase


class ExpenseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExpenseBinding

    private var totalExpense: Double = 0.00

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.editViewDateExpense.setText(DateCustom.dateCurrent())
        getExpenseTotal()


    }

    fun saveExpense(view: View) {
        if (validateFieldsExpense()) {
            val date: String = binding.editViewDateExpense.text.toString()
            val value = binding.editViewExpenseValue.text.toString().toDouble()
            val category = binding.editViewOutflowE.text.toString()
            val description = binding.editViewDescriptionE.text.toString()
            val type = "e"
            save(date, category, description, type, value)
        } else validateFieldsExpense()
    }

    private fun getExpenseTotal(): Double {
        userData().addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val expenseDb = snapshot.value
                totalExpense = expenseDb.toString().toDouble()
            }
        })
        return totalExpense
    }

    private fun userData(): DatabaseReference {
        val id = SettingsFirebase.id()
        return SettingsFirebase.fireBaseRef().child("users")
            .child(id)
            .child("totalExpense")

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

            val updateValue = value + totalExpense
            userData().setValue(updateValue)
            finish()
        }
    }

    private fun validateFieldsExpense(): Boolean {
        when {
            binding.editViewOutflowE.text!!.isEmpty() -> Toast.makeText(
                this,
                "Preecha a Categoria",
                Toast.LENGTH_LONG
            ).show()
            binding.editViewDescriptionE.text!!.isEmpty() -> Toast.makeText(
                this,
                "Preecha a Descrição",
                Toast.LENGTH_LONG
            ).show()
            binding.editViewExpenseValue.text.toString().isEmpty() -> Toast.makeText(
                this,
                "Preecha o valor",
                Toast.LENGTH_LONG
            ).show()
            binding.editViewDateExpense.text.toString().isEmpty() -> binding.editViewDateExpense.setText(DateCustom.dateCurrent())
            else -> return true

        }

        return false
    }


}























