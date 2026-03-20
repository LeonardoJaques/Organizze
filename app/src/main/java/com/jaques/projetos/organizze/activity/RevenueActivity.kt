package com.jaques.projetos.organizze.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.jaques.projetos.organizze.databinding.ActivityRevenueBinding
import com.jaques.projetos.organizze.helper.DatabaseHelper
import com.jaques.projetos.organizze.helper.DateCustom
import com.jaques.projetos.organizze.helper.SessionManager
import com.jaques.projetos.organizze.model.Movement
import com.jaques.projetos.organizze.model.MovementType
import com.jaques.projetos.organizze.repository.MovementRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RevenueActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRevenueBinding
    private lateinit var movementRepository: MovementRepository
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRevenueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val db = DatabaseHelper.getInstance(this)
        movementRepository = MovementRepository(db)
        sessionManager = SessionManager(this)

        binding.editViewDateRevenue.setText(DateCustom.dateCurrent())
    }

    fun saveRevenue(view: View) {
        if (!validateFields()) return

        val date = binding.editViewDateRevenue.text.toString()
        val movement = Movement(
            date = date,
            value = binding.editViewRevenueValue.text.toString().toDouble(),
            category = binding.editViewEntryR.text.toString(),
            description = binding.editViewDescriptionR.text.toString(),
            type = MovementType.REVENUE
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
                editViewEntryR.text.isNullOrEmpty() -> { showToast("Preencha a Categoria"); false }
                editViewDescriptionR.text.isNullOrEmpty() -> { showToast("Preencha a Descrição"); false }
                editViewRevenueValue.text.toString().isEmpty() -> { showToast("Preencha o valor"); false }
                editViewDateRevenue.text.toString().isEmpty() -> { editViewDateRevenue.setText(DateCustom.dateCurrent()); false }
                else -> true
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
