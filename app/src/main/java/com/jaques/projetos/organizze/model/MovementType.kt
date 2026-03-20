package com.jaques.projetos.organizze.model

enum class MovementType(val code: String) {
    EXPENSE("e"),
    REVENUE("r");

    companion object {
        fun fromCode(code: String): MovementType =
            entries.first { it.code == code }
    }
}
