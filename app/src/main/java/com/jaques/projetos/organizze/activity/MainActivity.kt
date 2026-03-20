package com.jaques.projetos.organizze.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.heinrichreimersoftware.materialintro.app.IntroActivity
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide
import com.jaques.projetos.organizze.R
import com.jaques.projetos.organizze.helper.SessionManager

class MainActivity : IntroActivity() {

    private lateinit var sessionManager: SessionManager

    private val introSlides = listOf(
        R.layout.intro_1,
        R.layout.intro_2,
        R.layout.intro_3,
        R.layout.intro_4,
        R.layout.intro_cadastro
    )

    override fun onStart() {
        super.onStart()
        if (sessionManager.isLoggedIn()) {
            startActivity(Intent(this, MajorActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(this)

        isButtonBackVisible = false
        isButtonNextVisible = false

        introSlides.forEach { layout ->
            val builder = FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(layout)

            if (layout == R.layout.intro_cadastro) {
                builder.canGoForward(false)
            }
            addSlide(builder.build())
        }
    }

    fun btEnter(view: View) = startActivity(Intent(this, LoginActivity::class.java))
    fun btRegister(view: View) = startActivity(Intent(this, RegisterActivity::class.java))
}
