package com.hloong.interview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hloong.interview.ui.custom.car_ani.CarAniActivity
import com.hloong.interview.ui.custom.mapview.MapChinaActivity
import com.hloong.interview.ui.custom.player.PlayerActivity
import com.hloong.interview.ui.custom.recyclerview.CustomRvActivity
import com.hloong.interview.ui.custom.ripple.RippleActivity
import com.hloong.interview.ui.custom.vlayout.VLayoutActivity
import com.hloong.interview.ui.recyclerview.RecyclerViewActivity
import com.hloong.interview.ui.splash_ani.SplashActivity
import com.hloong.interview.ui.split.SplitActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btSplash.setOnClickListener {
            startActivity(Intent(this,SplashActivity::class.java))
        }
        btSplit.setOnClickListener {
            startActivity(Intent(this,SplitActivity::class.java))
        }
        btCar.setOnClickListener {
            startActivity(Intent(this,CarAniActivity::class.java))
        }
        btRv.setOnClickListener {
            startActivity(Intent(this,RecyclerViewActivity::class.java))
        }
        btRvCustom.setOnClickListener {
            startActivity(Intent(this,CustomRvActivity::class.java))
        }
        btSvgMap.setOnClickListener {
            startActivity(Intent(this,MapChinaActivity::class.java))
        }
        btVLayout.setOnClickListener {
            startActivity(Intent(this,VLayoutActivity::class.java))
        }
        btPlayer.setOnClickListener {
            startActivity(Intent(this,PlayerActivity::class.java))
        }
        btRipple.setOnClickListener {
            startActivity(Intent(this,RippleActivity::class.java))
        }
    }


}