package com.jaques.projetos.organizze.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.jaques.projetos.organizze.R

class MajorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_major)
//        setSupportActionBar(findViewById(R.id.toolbar))
//
//        findViewById<FloatingActionButton>(R.id.fab_expense).setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
    }

    fun addExpense(view: View) =
        startActivity(Intent(this, ExpenseActivity::class.java))


    fun addRevenue(view: View) =
        startActivity(Intent(this, RevenueActivity::class.java))

}