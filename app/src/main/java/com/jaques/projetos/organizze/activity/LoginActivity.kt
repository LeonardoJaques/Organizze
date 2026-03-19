package com.jaques.projetos.organizze.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.*

import com.jaques.projetos.organizze.R
import com.jaques.projetos.organizze.databinding.ActivityLoginBinding
import com.jaques.projetos.organizze.model.UserOgzz
import com.jaques.projetos.organizze.settings.SettingsFirebase

private lateinit var userOgzz: UserOgzz


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonEnterLogin.setOnClickListener {
            val textEmail = binding.editEmailLogin.text.toString()
            val textPassword = binding.editPasswordLogin.text.toString()

            when {
                textEmail.isEmpty() -> Toast.makeText(
                    this,
                    "Preencha o email!",
                    Toast.LENGTH_LONG
                )
                    .show()
                textPassword.isEmpty() -> Toast.makeText(
                    this,
                    "Preencha a senha!",
                    Toast.LENGTH_LONG
                ).show()
                else -> {
                    userOgzz = UserOgzz()
                    userOgzz.email = textEmail
                    userOgzz.password = textPassword
                    validateLogin()
                }
            }
        }
    }

    private fun validateLogin() {
        val auth: FirebaseAuth = SettingsFirebase.getFirebaseAuthOrganizze()
        auth.signInWithEmailAndPassword(userOgzz.email, userOgzz.password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    var exception = ""
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        exception = "E-mail e senha não correspondem a um usuário cadastrado"
                    } catch (e: FirebaseAuthInvalidUserException) {
                        exception = "Usuario não está cadastrado"
                    } catch (e: FirebaseAuthActionCodeException) {
                        exception = "E-mail invalido ou expirado"
                    } catch (e: Exception) {
                        exception = "Erro ao cadastrar o usuário ${e.message}"
                        e.printStackTrace()
                    }
                    Toast.makeText(this, exception, Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        Toast.makeText(this, "Usuario Logado com sucesso", Toast.LENGTH_LONG).show()
        openMajorScream()
    }

    private fun openMajorScream() {
        startActivity(Intent(this, MajorActivity::class.java))
        finish()
    }
}