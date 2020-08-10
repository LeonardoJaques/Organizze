package com.jaques.projetos.organizze.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.heinrichreimersoftware.materialintro.app.IntroActivity
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide
import com.jaques.projetos.organizze.R
import com.jaques.projetos.organizze.settings.SettingsFirebase

class MainActivity : IntroActivity() {

    override fun onStart() {
        super.onStart()
        validadeUserLogin()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)


        isButtonBackVisible = false
        isButtonNextVisible = false

        for (i in 0..4) {
            val fragmentIntro = arrayListOf(
                R.layout.intro_1,
                R.layout.intro_2,
                R.layout.intro_3,
                R.layout.intro_4,
                R.layout.intro_cadastro
            )[i]

            if (fragmentIntro == R.layout.intro_cadastro) {
                addSlide(
                    FragmentSlide.Builder()
                        .background(android.R.color.white)
                        .canGoForward(false)
                        .fragment(fragmentIntro)
                        .build()
                )
            } else
                addSlide(
                    FragmentSlide.Builder()
                        .background(android.R.color.white)
                        .fragment(fragmentIntro)
                        .build()
                )
        }
    }


    fun btEnter(view: View) {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    fun btRegister(view: View) {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    fun validadeUserLogin() {
        val auth = SettingsFirebase.getFirebaseAuthOrganizze()
//        auth.signOut()
        if (auth.currentUser != null) openMajorScream()
    }

    private fun openMajorScream() {
        startActivity(Intent(this, MajorActivity::class.java))
    }
}