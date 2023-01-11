package com.hloong.interview.ui.custom.ripple

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hloong.interview.R
import kotlinx.android.synthetic.main.activity_ripple.*

class RippleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ripple)

        imageView.setOnClickListener {
            if (layout_RippleAnimation.isAnimationRunning){
                layout_RippleAnimation.stopRippleAnimation()
            }else{
                layout_RippleAnimation.startRippleAnimation()
            }
        }
    }
}