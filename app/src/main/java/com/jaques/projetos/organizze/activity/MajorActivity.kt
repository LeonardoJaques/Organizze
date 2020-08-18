package com.jaques.projetos.organizze.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jaques.projetos.organizze.R
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.android.synthetic.main.content_major.*

class MajorActivity : AppCompatActivity() {

    private lateinit var calendar: MaterialCalendarView
    private lateinit var textWelcome: TextView
    private lateinit var textBalance: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_major)
//        setSupportActionBar(findViewById(R.id.toolbar))

        textBalance = textView_Balance_Major
        textWelcome = textWelcomeUser_Major
        calendar = calendarView
        settingsCalendar()


    }

    fun settingsCalendar() {


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

}