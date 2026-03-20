package com.jaques.projetos.organizze.repository

import com.jaques.projetos.organizze.helper.DatabaseHelper
import com.jaques.projetos.organizze.model.AuthResult
import com.jaques.projetos.organizze.model.User

class UserRepository(private val db: DatabaseHelper) {

    fun register(name: String, email: String, password: String): AuthResult {
        if (db.emailExists(email)) {
            return AuthResult.Error("Essa conta já foi cadastrada")
        }
        return try {
            val userId = db.registerUser(name, email, password)
            val user = db.getUserById(userId)!!
            AuthResult.Success(user)
        } catch (e: Exception) {
            AuthResult.Error("Erro ao cadastrar: ${e.message}")
        }
    }

    fun login(email: String, password: String): AuthResult {
        val user = db.loginUser(email, password)
            ?: return AuthResult.Error("E-mail e senha não correspondem a um usuário cadastrado")
        return AuthResult.Success(user)
    }

    fun getUser(userId: Long): User? = db.getUserById(userId)
}
