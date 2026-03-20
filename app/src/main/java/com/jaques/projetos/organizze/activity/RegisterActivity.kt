package com.jaques.projetos.organizze.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.jaques.projetos.organizze.databinding.ActivityRegisterBinding
import com.jaques.projetos.organizze.helper.DatabaseHelper
import com.jaques.projetos.organizze.helper.SessionManager
import com.jaques.projetos.organizze.model.AuthResult
import com.jaques.projetos.organizze.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var userRepository: UserRepository
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Cadastro"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val db = DatabaseHelper.getInstance(this)
        userRepository = UserRepository(db)
        sessionManager = SessionManager(this)

        binding.buttonRegisterView.setOnClickListener { handleRegister() }
    }

    private fun handleRegister() {
        val name = binding.editName.text.toString().trim()
        val email = binding.editEmail.text.toString().trim()
        val password = binding.editPassword.text.toString()

        when {
            name.isEmpty() -> showToast("Preencha o nome!")
            email.isEmpty() -> showToast("Preencha o email!")
            password.isEmpty() -> showToast("Preencha a senha!")
            password.length < 6 -> showToast("Digite uma senha mais forte!")
            else -> {
                binding.buttonRegisterView.isEnabled = false
                lifecycleScope.launch {
                    val result = withContext(Dispatchers.IO) {
                        userRepository.register(name, email, password)
                    }
                    binding.buttonRegisterView.isEnabled = true
                    when (result) {
                        is AuthResult.Success -> {
                            sessionManager.createSession(result.user.id)
                            showToast("Cadastrado com sucesso!")
                            startActivity(Intent(this@RegisterActivity, MajorActivity::class.java))
                            finish()
                        }
                        is AuthResult.Error -> showToast(result.message)
                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
