package com.jaques.projetos.organizze.model

data class Movement(
    val id: Long = 0,
    val date: String,
    val category: String,
    val description: String,
    val type: MovementType,
    val value: Double
)
