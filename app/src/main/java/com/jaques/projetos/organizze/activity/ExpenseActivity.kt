package com.jaques.projetos.organizze.activity

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.jaques.projetos.organizze.R
import com.jaques.projetos.organizze.helper.DateCustom
import com.jaques.projetos.organizze.model.Movement
import kotlinx.android.synthetic.main.activity_expense.*

class ExpenseActivity : AppCompatActivity() {

    private lateinit var fieldDate: TextInputEditText
    private lateinit var fieldCategory: TextInputEditText
    private lateinit var fieldDescription: TextInputEditText
    private lateinit var fieldValue: EditText

    private lateinit var floatingActionButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense)

        fieldValue = editViewExpenseValue
        fieldDate = editViewDateExpense
        fieldCategory = editViewOutflow
        fieldDescription = editViewDescription

        fieldDate.setText(DateCustom.dateCurrent())
        floatingActionButton = fabSaveExpenseView


        floatingActionButton.setOnClickListener {
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
                else -> saveExpense()
            }
        }
    }

    private fun saveExpense() {
        val movement = Movement()
        val category = fieldCategory.text
        val description = fieldDescription.text
        val value = fieldValue.text.toString().toDouble()
        val date = fieldDate.text.toString()
        movement.saveMovementOgzz(category, description, value, date)

    }

}

















