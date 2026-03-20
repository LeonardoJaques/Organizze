package com.jaques.projetos.organizze.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.jaques.projetos.organizze.databinding.ActivityLoginBinding
import com.jaques.projetos.organizze.helper.DatabaseHelper
import com.jaques.projetos.organizze.helper.SessionManager
import com.jaques.projetos.organizze.model.AuthResult
import com.jaques.projetos.organizze.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var userRepository: UserRepository
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val db = DatabaseHelper.getInstance(this)
        userRepository = UserRepository(db)
        sessionManager = SessionManager(this)

        binding.textViewGoToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.buttonEnterLogin.setOnClickListener { handleLogin() }
    }

    private fun handleLogin() {
        val email = binding.editEmailLogin.text.toString().trim()
        val password = binding.editPasswordLogin.text.toString()

        when {
            email.isEmpty() -> showToast("Preencha o email!")
            password.isEmpty() -> showToast("Preencha a senha!")
            else -> {
                binding.buttonEnterLogin.isEnabled = false
                lifecycleScope.launch {
                    val result = withContext(Dispatchers.IO) {
                        userRepository.login(email, password)
                    }
                    binding.buttonEnterLogin.isEnabled = true
                    when (result) {
                        is AuthResult.Success -> {
                            sessionManager.createSession(result.user.id)
                            showToast("Logado com sucesso!")
                            startActivity(Intent(this@LoginActivity, MajorActivity::class.java))
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
