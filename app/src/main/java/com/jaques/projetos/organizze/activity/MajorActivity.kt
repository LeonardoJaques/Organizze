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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.jaques.projetos.organizze.R
import com.jaques.projetos.organizze.adapter.MovementAdapter
import com.jaques.projetos.organizze.model.Movement
import com.jaques.projetos.organizze.settings.SettingsFirebase
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.android.synthetic.main.activity_major.*
import kotlinx.android.synthetic.main.content_major.*
import java.lang.Double.parseDouble
import java.util.logging.Level.parse
import kotlin.math.absoluteValue

class MajorActivity : AppCompatActivity() {

    private lateinit var calendar: MaterialCalendarView
    private lateinit var textWelcome: TextView
    private lateinit var textBalance: TextView

    private lateinit var databaseListenerUser: ValueEventListener
    private lateinit var databaseListenerMove: ValueEventListener

    private lateinit var recycleView: RecyclerView
    private lateinit var movementAdapter: MovementAdapter
    private var movementList: ArrayList<Movement> = arrayListOf()

    private lateinit var selectMonthYear: String

    private var firebaseRef = SettingsFirebase.getFirebaseRefenceOrganizze().reference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_major)

        val toolbar = toolbarView
        toolbar.title = "Organizze"
        setSupportActionBar(toolbar)



        textBalance = textView_Balance_Major
        textWelcome = textWelcomeUser_Major
        calendar = calendarView
        recycleView = RecyclerViewMovement
        settingsCalendar()

        movementAdapter = MovementAdapter(movementList)


        val recyclerViewLayoutManager = LinearLayoutManager(this)
        recycleView.layoutManager = recyclerViewLayoutManager
        recycleView.setHasFixedSize(true)
        recycleView.adapter = movementAdapter

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

        val dateCurrent: CalendarDay = calendar.currentDate

        selectMonthYear =
            ("${String.format("%02d", dateCurrent.month)}${dateCurrent.year}").toString()
        calendar.setOnMonthChangedListener { _, date ->
            selectMonthYear = ("${String.format("%02d", date.month)}${date.year}").toString()
            firebaseRef.removeEventListener(databaseListenerMove)
            movData()
        }
    }


    fun addExpense(view: View) =
        startActivity(Intent(this, ExpenseActivity::class.java))

    fun addRevenue(view: View) =
        startActivity(Intent(this, RevenueActivity::class.java))

    private fun userData() {
        val userDataRef = firebaseRef.child("users").child(SettingsFirebase.id())
        Log.i("Firebase", "GetPath -> $userDataRef")
        databaseListenerUser = userDataRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val useNameFirebase = snapshot.child("name").value
                val totalExpense = snapshot.child("totalExpense").value
                val totalRevenue = snapshot.child("totalRevenue").value
                val totalExtract =
                    totalRevenue.toString().toDouble() - totalExpense.toString().toDouble()

                val decimalFormat = DecimalFormat("0.00")
                val value = decimalFormat.format(totalExtract).toString()
                textBalance.text = "R$ ${value}"
//                textBalance.text = "R$ ${totalExtract.absoluteValue}"
                textWelcome.text = "Olá, ${useNameFirebase.toString()}"
            }
        })

    }


    private fun movData() {
        val movDataRef =
            firebaseRef.child("movement").child(SettingsFirebase.id()).child(selectMonthYear)
        Log.i("Firebase", "GetPath -> $movDataRef")
        databaseListenerMove = movDataRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                movementList.clear()

                for (postSnapshot in snapshot.children) {
                    val date = postSnapshot.child("date").value.toString()
                    val category = postSnapshot.child("category").value.toString()
                    val description = postSnapshot.child("description").value.toString()
                    val type = postSnapshot.child("type").value.toString()
                    val value = postSnapshot.child("value").value.toString()

                    val movement = Movement(date, category, description, type, value)

                    Log.i("Firebase", "GetFirebase -> ${value}")
                    movementList.add(movement)
                }
                movementAdapter.notifyDataSetChanged()
            }
        })
    }


    override fun onStart() {
        userData()
        movData()
        Log.i("Evento", "Evento foi iniciado")
        super.onStart()
    }

    override fun onStop() {
        firebaseRef.removeEventListener(databaseListenerUser)
        firebaseRef.removeEventListener(databaseListenerMove)
        Log.i("Evento", "Evento foi removido")
        super.onStop()
    }


}


