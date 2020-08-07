package com.jaques.projetos.organizze.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*

import com.jaques.projetos.organizze.R
import com.jaques.projetos.organizze.model.User
import com.jaques.projetos.organizze.settings.SettingsFirebase
import kotlinx.android.synthetic.main.activity_login.*

private lateinit var fieldEmailLogin: EditText
private lateinit var fieldPasswordLogin: EditText
private lateinit var btEnterLogin: Button
private lateinit var user: User


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        fieldEmailLogin = editEmailLogin
        fieldPasswordLogin = editPasswordLogin
        btEnterLogin = buttonEnterLogin

        btEnterLogin.setOnClickListener {
            val textEmail = fieldEmailLogin.text.toString()
            val textPassword = fieldPasswordLogin.text.toString()

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
                    user = User()
                    user.email = textEmail
                    user.password = textPassword
                    validateLogin()
                }
            }
        }
    }

    private fun validateLogin() {
        val auth: FirebaseAuth = SettingsFirebase.getFirebaseAuthOrganizze()
        auth.signInWithEmailAndPassword(user.email, user.password)
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
    }

}