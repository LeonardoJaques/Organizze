package com.jaques.projetos.organizze.model

data class User(
    val id: Long = 0,
    val name: String,
    val email: String,
    val totalExpense: Double = 0.0,
    val totalRevenue: Double = 0.0
) {
    val balance: Double get() = totalRevenue - totalExpense
}
