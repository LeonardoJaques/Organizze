package com.jaques.projetos.organizze

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.heinrichreimersoftware.materialintro.app.IntroActivity
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide

class MainActivity : IntroActivity() {
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

        fun btEnter(view: View){
//            startActivity(Intent(this,))
        }

        fun btRegister(view: View){}

    }
}