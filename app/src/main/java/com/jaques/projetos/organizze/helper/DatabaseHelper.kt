package com.jaques.projetos.organizze.helper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.jaques.projetos.organizze.model.Movement
import com.jaques.projetos.organizze.model.MovementType
import com.jaques.projetos.organizze.model.User
import java.security.MessageDigest

class DatabaseHelper private constructor(context: Context) :
    SQLiteOpenHelper(context.applicationContext, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "organizze.db"
        private const val DATABASE_VERSION = 2
        private const val TABLE_USERS = "users"
        private const val TABLE_MOVEMENTS = "movements"

        @Volatile
        private var instance: DatabaseHelper? = null

        fun getInstance(context: Context): DatabaseHelper =
            instance ?: synchronized(this) {
                instance ?: DatabaseHelper(context).also { instance = it }
            }

        fun hashPassword(password: String): String {
            val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
            return bytes.joinToString("") { "%02x".format(it) }
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """CREATE TABLE $TABLE_USERS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                email TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL,
                totalExpense REAL DEFAULT 0.0,
                totalRevenue REAL DEFAULT 0.0
            )"""
        )
        db.execSQL(
            """CREATE TABLE $TABLE_MOVEMENTS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                userId INTEGER NOT NULL,
                date TEXT NOT NULL,
                category TEXT NOT NULL,
                description TEXT NOT NULL,
                type TEXT NOT NULL,
                value REAL NOT NULL,
                monthYear TEXT NOT NULL,
                FOREIGN KEY(userId) REFERENCES $TABLE_USERS(id)
            )"""
        )
        createIndexes(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            createIndexes(db)
        }
    }

    override fun onOpen(db: SQLiteDatabase) {
        super.onOpen(db)
        db.setForeignKeyConstraintsEnabled(true)
    }

    private fun createIndexes(db: SQLiteDatabase) {
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_movements_userId ON $TABLE_MOVEMENTS(userId)")
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_movements_monthYear ON $TABLE_MOVEMENTS(monthYear)")
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_movements_user_month ON $TABLE_MOVEMENTS(userId, monthYear)")
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_users_email ON $TABLE_USERS(email)")
    }

    // ---- Users ----

    fun registerUser(name: String, email: String, password: String): Long {
        val values = ContentValues().apply {
            put("name", name)
            put("email", email)
            put("password", hashPassword(password))
            put("totalExpense", 0.0)
            put("totalRevenue", 0.0)
        }
        return writableDatabase.insertOrThrow(TABLE_USERS, null, values)
    }

    fun loginUser(email: String, password: String): User? =
        readableDatabase.rawQuery(
            "SELECT * FROM $TABLE_USERS WHERE email = ? AND password = ?",
            arrayOf(email, hashPassword(password))
        ).use { it.toUser() }

    fun getUserById(userId: Long): User? =
        readableDatabase.rawQuery(
            "SELECT * FROM $TABLE_USERS WHERE id = ?",
            arrayOf(userId.toString())
        ).use { it.toUser() }

    fun emailExists(email: String): Boolean =
        readableDatabase.rawQuery(
            "SELECT id FROM $TABLE_USERS WHERE email = ?",
            arrayOf(email)
        ).use { it.moveToFirst() }

    fun incrementTotal(userId: Long, column: String, amount: Double) {
        writableDatabase.execSQL(
            "UPDATE $TABLE_USERS SET $column = $column + ? WHERE id = ?",
            arrayOf(amount, userId)
        )
    }

    fun recalculateTotals(userId: Long) {
        writableDatabase.execSQL(
            """UPDATE $TABLE_USERS SET
                totalExpense = COALESCE((SELECT SUM(value) FROM $TABLE_MOVEMENTS WHERE userId = ? AND type = 'e'), 0.0),
                totalRevenue = COALESCE((SELECT SUM(value) FROM $TABLE_MOVEMENTS WHERE userId = ? AND type = 'r'), 0.0)
            WHERE id = ?""",
            arrayOf(userId, userId, userId)
        )
    }

    // ---- Movements ----

    fun insertMovement(
        userId: Long, date: String, category: String,
        description: String, type: String, value: Double, monthYear: String
    ): Long = writableDatabase.insert(
        TABLE_MOVEMENTS, null,
        ContentValues().apply {
            put("userId", userId)
            put("date", date)
            put("category", category)
            put("description", description)
            put("type", type)
            put("value", value)
            put("monthYear", monthYear)
        }
    )

    fun getMovementsByMonthYear(userId: Long, monthYear: String): List<Movement> {
        val list = mutableListOf<Movement>()
        readableDatabase.rawQuery(
            "SELECT * FROM $TABLE_MOVEMENTS WHERE userId = ? AND monthYear = ? ORDER BY id DESC",
            arrayOf(userId.toString(), monthYear)
        ).use { cursor ->
            while (cursor.moveToNext()) {
                list.add(cursor.toMovement())
            }
        }
        return list
    }

    fun deleteMovement(movementId: Long) {
        writableDatabase.delete(TABLE_MOVEMENTS, "id = ?", arrayOf(movementId.toString()))
    }

    fun <T> runInTransaction(block: () -> T): T {
        val db = writableDatabase
        db.beginTransaction()
        return try {
            val result = block()
            db.setTransactionSuccessful()
            result
        } finally {
            db.endTransaction()
        }
    }

    // ---- Extensions ----

    private fun Cursor.toUser(): User? =
        if (moveToFirst()) User(
            id = getLong(getColumnIndexOrThrow("id")),
            name = getString(getColumnIndexOrThrow("name")),
            email = getString(getColumnIndexOrThrow("email")),
            totalExpense = getDouble(getColumnIndexOrThrow("totalExpense")),
            totalRevenue = getDouble(getColumnIndexOrThrow("totalRevenue"))
        ) else null

    private fun Cursor.toMovement(): Movement = Movement(
        id = getLong(getColumnIndexOrThrow("id")),
        date = getString(getColumnIndexOrThrow("date")),
        category = getString(getColumnIndexOrThrow("category")),
        description = getString(getColumnIndexOrThrow("description")),
        type = MovementType.fromCode(getString(getColumnIndexOrThrow("type"))),
        value = getDouble(getColumnIndexOrThrow("value"))
    )
}
