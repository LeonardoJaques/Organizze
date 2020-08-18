package com.jaques.projetos.organizze.activity

import android.content.Intent
import android.icu.text.DecimalFormat
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.jaques.projetos.organizze.R
import com.jaques.projetos.organizze.helper.Base64Custom
import com.jaques.projetos.organizze.settings.SettingsFirebase
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.android.synthetic.main.activity_major.*
import kotlinx.android.synthetic.main.content_major.*

class MajorActivity : AppCompatActivity() {

    private lateinit var calendar: MaterialCalendarView
    private lateinit var textWelcome: TextView
    private lateinit var textBalance: TextView

    private lateinit var databaseListener: ValueEventListener

    override fun onStart() {
        userData()
        Log.i("Evento", "Evento foi iniciado")
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_major)

        val toolbar = toolbarView
        toolbar.title = "Organizze"
        setSupportActionBar(toolbar)

        textBalance = textView_Balance_Major
        textWelcome = textWelcomeUser_Major
        calendar = calendarView
        settingsCalendar()

        databaseListener = userData().addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val useNameFirebase = snapshot.child("name").value
                val totalExpense = snapshot.child("totalExpense").value.toString().toDouble()
                val totalRevenue = snapshot.child("totalRevenue").value.toString().toDouble()
                val totalExtract = totalRevenue - totalExpense

                val decimalFormat = DecimalFormat("0.00")

                textBalance.text = "R$ ${decimalFormat.format(totalExtract)}"
                textWelcome.text = "Olá, ${useNameFirebase.toString()}"

            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_major, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuSignout -> {
                SettingsFirebase.getFirebaseAuthOrganizze().signOut()
                Toast.makeText(this, "Deslogado com sucesso", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }

        }

        return super.onOptionsItemSelected(item)
    }


    private fun settingsCalendar() {

        calendar.setTitleMonths(
            arrayOf<CharSequence>(
                "Janeiro", "Fevereiro", "Março", "Abril", "Maio",
                "Junho", "Julho", "Agosto", "Setembro", "Outubro",
                "Novembro", "Dezembro"
            )
        )

        calendar.setWeekDayLabels(
            arrayOf<CharSequence>(
                "S", "T", "Q", "Q",
                "S", "S", "D"
            )
        )

        calendar.setOnMonthChangedListener { widget, date ->
            Log.i("data:", " Valor 1:${date.month}/${date.year}")
        }
    }


    fun addExpense(view: View) =
        startActivity(Intent(this, ExpenseActivity::class.java))


    fun addRevenue(view: View) =
        startActivity(Intent(this, RevenueActivity::class.java))


    private fun userData(): DatabaseReference {
        val database = SettingsFirebase.getFirebaseRefenceOrganizze().reference
        val auth: FirebaseAuth = SettingsFirebase.getFirebaseAuthOrganizze()
        val id = Base64Custom.codeBase64(auth.currentUser!!.email.toString())
        val dataBaseListener = database.child("users").child(id)
        return dataBaseListener
    }

    override fun onStop() {
        userData().removeEventListener(databaseListener)
        Log.i("Evento", "Evento foi removido")
        super.onStop()
    }

}