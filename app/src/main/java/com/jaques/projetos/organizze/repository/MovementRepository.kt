package com.jaques.projetos.organizze.repository

import com.jaques.projetos.organizze.helper.DatabaseHelper
import com.jaques.projetos.organizze.model.Movement
import com.jaques.projetos.organizze.model.MovementType

class MovementRepository(private val db: DatabaseHelper) {

    fun getByMonthYear(userId: Long, monthYear: String): List<Movement> =
        db.getMovementsByMonthYear(userId, monthYear)

    fun save(userId: Long, movement: Movement, monthYear: String) {
        db.runInTransaction {
            db.insertMovement(
                userId, movement.date, movement.category,
                movement.description, movement.type.code, movement.value, monthYear
            )
            val column = when (movement.type) {
                MovementType.EXPENSE -> "totalExpense"
                MovementType.REVENUE -> "totalRevenue"
            }
            db.incrementTotal(userId, column, movement.value)
        }
    }

    fun delete(userId: Long, movement: Movement) {
        db.runInTransaction {
            db.deleteMovement(movement.id)
            db.recalculateTotals(userId)
        }
    }
}
