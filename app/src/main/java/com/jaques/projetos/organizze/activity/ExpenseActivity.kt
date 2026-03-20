package com.jaques.projetos.organizze.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.jaques.projetos.organizze.databinding.ActivityExpenseBinding
import com.jaques.projetos.organizze.helper.DatabaseHelper
import com.jaques.projetos.organizze.helper.DateCustom
import com.jaques.projetos.organizze.helper.SessionManager
import com.jaques.projetos.organizze.model.Movement
import com.jaques.projetos.organizze.model.MovementType
import com.jaques.projetos.organizze.repository.MovementRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExpenseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExpenseBinding
    private lateinit var movementRepository: MovementRepository
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val db = DatabaseHelper.getInstance(this)
        movementRepository = MovementRepository(db)
        sessionManager = SessionManager(this)

        binding.editViewDateExpense.setText(DateCustom.dateCurrent())
    }

    fun saveExpense(view: View) {
        if (!validateFields()) return

        val date = binding.editViewDateExpense.text.toString()
        val movement = Movement(
            date = date,
            value = binding.editViewExpenseValue.text.toString().toDouble(),
            category = binding.editViewOutflowE.text.toString(),
            description = binding.editViewDescriptionE.text.toString(),
            type = MovementType.EXPENSE
        )

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                movementRepository.save(sessionManager.getUserId(), movement, DateCustom.dateChoose(date))
            }
            finish()
        }
    }

    private fun validateFields(): Boolean {
        with(binding) {
            return when {
                editViewOutflowE.text.isNullOrEmpty() -> { showToast("Preencha a Categoria"); false }
                editViewDescriptionE.text.isNullOrEmpty() -> { showToast("Preencha a Descrição"); false }
                editViewExpenseValue.text.toString().isEmpty() -> { showToast("Preencha o valor"); false }
                editViewDateExpense.text.toString().isEmpty() -> { editViewDateExpense.setText(DateCustom.dateCurrent()); false }
                else -> true
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
