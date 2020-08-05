package com.jaques.projetos.organizze

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.heinrichreimersoftware.materialintro.app.IntroActivity
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide

class MainActivity : IntroActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        isButtonBackVisible = false
        isButtonNextVisible = false

        for (i in 0..3){
            val fragmentIntro = arrayListOf(R.layout.intro_1,
                                                 R.layout.intro_2,
                                                 R.layout.intro_3,
                                                 R.layout.intro_4 )[i]

            addSlide( FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(fragmentIntro)
                .build()
            )
        }


    }
}