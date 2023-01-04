package com.hloong.interview.ui.split

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hloong.interview.R

class SplitActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(SplitView(this))
    }
}