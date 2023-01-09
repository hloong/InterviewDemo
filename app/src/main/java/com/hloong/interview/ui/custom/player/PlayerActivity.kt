package com.hloong.interview.ui.custom.player

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hloong.interview.R


class PlayerActivity : AppCompatActivity() {
    val PARAM_MUSIC_LIST = "PARAM_MUSIC_LIST"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
    }

}