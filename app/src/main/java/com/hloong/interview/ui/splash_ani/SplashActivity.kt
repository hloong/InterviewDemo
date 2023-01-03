package com.hloong.interview.ui.splash_ani

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.hloong.interview.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val container = findViewById<View>(R.id.parallax_container) as ParallaxContainer
        container.setUp(
            *intArrayOf(
                R.layout.view_intro_1,
                R.layout.view_intro_2,
                R.layout.view_intro_3,
                R.layout.view_intro_4,
                R.layout.view_intro_5,
                R.layout.view_intro_6,
                R.layout.view_intro_7,
                R.layout.view_login
            )
        )
        iv_man.setBackgroundResource(R.drawable.man_run)
        container.setIv_man(iv_man)
    }
}