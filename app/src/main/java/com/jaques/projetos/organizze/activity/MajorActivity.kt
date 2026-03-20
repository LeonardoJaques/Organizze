package com.jaques.projetos.organizze.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jaques.projetos.organizze.R
import com.jaques.projetos.organizze.adapter.MovementAdapter
import com.jaques.projetos.organizze.databinding.ActivityMajorBinding
import com.jaques.projetos.organizze.databinding.ContentMajorBinding
import com.jaques.projetos.organizze.helper.DatabaseHelper
import com.jaques.projetos.organizze.helper.SessionManager
import com.jaques.projetos.organizze.helper.toBRL
import com.jaques.projetos.organizze.model.Movement
import com.jaques.projetos.organizze.repository.MovementRepository
import com.jaques.projetos.organizze.repository.UserRepository
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MajorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMajorBinding
    private lateinit var contentBinding: ContentMajorBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var userRepository: UserRepository
    private lateinit var movementRepository: MovementRepository

    private lateinit var movementAdapter: MovementAdapter
    private val movementList = mutableListOf<Movement>()

    private lateinit var selectedMonthYear: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMajorBinding.inflate(layoutInflater)
        contentBinding = binding.contentMajor
        setContentView(binding.root)

        val db = DatabaseHelper.getInstance(this)
        sessionManager = SessionManager(this)
        userRepository = UserRepository(db)
        movementRepository = MovementRepository(db)

        binding.toolbarView.title = "Organizze"
        setSupportActionBar(binding.toolbarView)

        setupCalendar()
        setupRecyclerView()
        setupSwipeToDelete()
    }

    private fun setupRecyclerView() {
        movementAdapter = MovementAdapter(movementList)
        contentBinding.RecyclerViewMovement.apply {
            layoutManager = LinearLayoutManager(this@MajorActivity)
            setHasFixedSize(true)
            adapter = movementAdapter
        }
    }

    private fun setupSwipeToDelete() {
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START or ItemTouchHelper.END) {
            override fun onMove(rv: RecyclerView, vh: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val movement = movementList[position]
                val userId = sessionManager.getUserId()

                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        movementRepository.delete(userId, movement)
                    }
                    movementList.removeAt(position)
                    movementAdapter.notifyItemRemoved(position)
                    loadUserData()
                    Toast.makeText(this@MajorActivity, "Removido", Toast.LENGTH_SHORT).show()
                }
            }
        }
        ItemTouchHelper(callback).attachToRecyclerView(contentBinding.RecyclerViewMovement)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_major, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menuSignout) {
            sessionManager.logout()
            Toast.makeText(this, "Deslogado com sucesso", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupCalendar() {
        val months = arrayOf<CharSequence>(
            "Janeiro", "Fevereiro", "Março", "Abril", "Maio",
            "Junho", "Julho", "Agosto", "Setembro", "Outubro",
            "Novembro", "Dezembro"
        )
        contentBinding.calendarView.setTitleMonths(months)

        val current: CalendarDay = contentBinding.calendarView.currentDate
        selectedMonthYear = "%02d%d".format(current.month, current.year)

        contentBinding.calendarView.setOnMonthChangedListener { _, date ->
            selectedMonthYear = "%02d%d".format(date.month, date.year)
            loadMovements()
        }
    }

    fun addExpense(view: View) = startActivity(Intent(this, ExpenseActivity::class.java))
    fun addRevenue(view: View) = startActivity(Intent(this, RevenueActivity::class.java))

    private fun loadUserData() {
        lifecycleScope.launch {
            val user = withContext(Dispatchers.IO) {
                userRepository.getUser(sessionManager.getUserId())
            } ?: return@launch
            contentBinding.textViewBalanceMajor.text = user.balance.toBRL()
            contentBinding.textWelcomeUserMajor.text = "Olá, ${user.name}"
        }
    }

    private fun loadMovements() {
        lifecycleScope.launch {
            val movements = withContext(Dispatchers.IO) {
                movementRepository.getByMonthYear(sessionManager.getUserId(), selectedMonthYear)
            }
            movementAdapter.submitList(movements)
        }
    }

    override fun onStart() {
        super.onStart()
        loadUserData()
        loadMovements()
    }
}
