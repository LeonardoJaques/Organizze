package com.jaques.projetos.organizze.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.jaques.projetos.organizze.R
import com.jaques.projetos.organizze.databinding.ActivityRegisterBinding
import com.jaques.projetos.organizze.helper.Base64Custom
import com.jaques.projetos.organizze.model.UserOgzz
import com.jaques.projetos.organizze.settings.SettingsFirebase


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var userOgzz: UserOgzz


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.title = "Cadastro"

        binding.buttonRegisterView.setOnClickListener {
            val textName = binding.editName.text.toString()
            val textEmail = binding.editEmail.text.toString()
            val textPassword = binding.editPassword.text.toString()

            when {
                textName.isEmpty() -> Toast.makeText(
                    this,
                    "Preencha o nome!",
                    Toast.LENGTH_LONG
                )
                    .show()
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
                    userOgzz = UserOgzz(textName, textEmail, textPassword)
                    registerUser()
                }
            }
        }
    }


    private fun registerUser() {
        val auth = SettingsFirebase.getFirebaseAuthOrganizze()
        auth.createUserWithEmailAndPassword(userOgzz.email, userOgzz.password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    updateUI(user)
                    userOgzz.saveUserOgzz()

                } else {
                    var exception = ""
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        exception = "Digite uma senha mais forte!"
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        exception = "Por favor, digite um e-mail válido"
                    } catch (e: FirebaseAuthUserCollisionException) {
                        exception = "Essa conta já foi cadastrada"
                    } catch (e: Exception) {
                        exception = "Erro ao cadastrar o usuário ${e.message}"
                        e.printStackTrace()
                    }
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext, exception,
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        Toast.makeText(this, "Usuario cadastrado com sucesso", Toast.LENGTH_LONG).show()
        openMajorScream()
    }

    private fun openMajorScream() {
        startActivity(Intent(this, MajorActivity::class.java))
        finish()
    }


}


