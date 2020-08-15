package com.jaques.projetos.organizze.activity

import android.os.Bundle
import android.widget.EditText
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

            saveExpense()
        }
    }

    private fun saveExpense() {
        val movement = Movement()


        val category = fieldCategory.text
        val description = fieldDescription.text
        val value = fieldValue.text.toString().toDouble()
        val date = fieldDate.text

        movement.saveMovementOgzz(category, description, value, date)


    }

}

















