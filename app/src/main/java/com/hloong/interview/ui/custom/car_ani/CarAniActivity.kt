package com.hloong.interview.ui.custom.car_ani

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class CarAniActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(CarView(this))
    }
}