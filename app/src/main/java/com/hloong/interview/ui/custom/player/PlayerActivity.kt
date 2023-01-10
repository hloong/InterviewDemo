package com.hloong.interview.ui.custom.player

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.hloong.interview.R
import kotlinx.android.synthetic.main.activity_player.*

class PlayerActivity : AppCompatActivity() {
    val PARAM_MUSIC_LIST = "PARAM_MUSIC_LIST"
    val MUSIC_MESSAGE = 0
    var mMusicDatas = ArrayList<MusicData>()
    private var mMusicReceiver = MusicReceiver()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        initMusicDatas()
        initView()
        initMusicReceiver()
        makeStatusBarTransparent()
    }

    private fun makeStatusBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    private fun initView() {
        setSupportActionBar(toolBar)
        (discview as DiscView).setPlayInfoListener(object: DiscView.IPlayInfo{
            override fun onMusicPicChanged(musicPicRes: Int) {
                try2UpdateMusicPicBackground(musicPicRes)
            }
            override fun onMusicChanged(musicChangedStatus: DiscView.MusicChangedStatus?) {
                when (musicChangedStatus) {
                    DiscView.MusicChangedStatus.PLAY -> {
                        play()
                    }
                    DiscView.MusicChangedStatus.PAUSE -> {
                        pause()
                    }
                    DiscView.MusicChangedStatus.NEXT -> {
                        next()
                    }
                    DiscView.MusicChangedStatus.LAST -> {
                        last()
                    }
                    DiscView.MusicChangedStatus.STOP -> {
                        stop()
                    }
                }
            }
            override fun onMusicInfoChanged(musicName: String?, musicAuthor: String?) {
                supportActionBar!!.title = musicName
                supportActionBar!!.subtitle = musicAuthor
            }
        })
        musicSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvCurrentTime.text = duration2Time(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                startUpdateSeekBarProgress()
                seekTo(seekBar!!.progress)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                stopUpdateSeekBarProgree()
            }

        })

        ivLast.setOnClickListener {
            (discview as DiscView).last()
        }
        ivNext.setOnClickListener {
            (discview as DiscView).next()
        }
        ivPlayOrPause.setOnClickListener {
            (discview as DiscView).playOrPause()
        }

    }
    private fun seekTo(position: Int) {
        val intent = Intent(MusicService.ACTION_OPT_MUSIC_SEEK_TO)
        intent.putExtra(MusicService.PARAM_MUSIC_SEEK_TO, position)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    private fun try2UpdateMusicPicBackground(musicPicRes: Int) {
        if (rootLayout.isNeed2UpdateBackground(musicPicRes)){
            Thread(Runnable {
                val foregroundDrawable: Drawable = getForegroundDrawable(musicPicRes)
                runOnUiThread {
                    rootLayout.foreground = foregroundDrawable
                    rootLayout.beginAnimation()
                }
            }).start()
        }
    }
    private fun getForegroundDrawable(musicPicRes: Int):Drawable{
        /*得到屏幕的宽高比，以便按比例切割图片一部分*/
        val widthHeightSize = (DisplayUtil.getScreenWidth(this)
                * 1.0 / DisplayUtil.getScreenHeight(this) * 1.0).toFloat()
        val bitmap = getForegroundBitmap(musicPicRes)
        val cropBitmapWidth = (widthHeightSize * bitmap.height).toInt()
        val cropBitmapWidthX = ((bitmap.width - cropBitmapWidth) / 2.0).toInt()
        /*切割部分图片*/
        val cropBitmap = Bitmap.createBitmap(bitmap, cropBitmapWidthX, 0, cropBitmapWidth, bitmap.height)
        /*缩小图片*/
        /*缩小图片*/
        val scaleBitmap = Bitmap.createScaledBitmap(
            cropBitmap, bitmap.width / 50, bitmap.height / 50, false
        )
        /*模糊化*/
        val blurBitmap = FastBlurUtil.doBlur(scaleBitmap, 8, true)
        val foregroundDrawable: Drawable = BitmapDrawable(blurBitmap)
        /*加入灰色遮罩层，避免图片过亮影响其他控件*/
        /*加入灰色遮罩层，避免图片过亮影响其他控件*/
        foregroundDrawable.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY)
        return foregroundDrawable
    }


    private fun getForegroundBitmap(musicPicRes: Int):Bitmap{
        val screenWidth = DisplayUtil.getScreenWidth(this)
        val screenHeight = DisplayUtil.getScreenHeight(this)

        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true

        BitmapFactory.decodeResource(resources, musicPicRes, options)
        val imageWidth = options.outWidth
        val imageHeight = options.outHeight

        if (imageWidth < screenWidth && imageHeight < screenHeight) {
            return BitmapFactory.decodeResource(resources, musicPicRes)
        }

        var sample = 2
        val sampleX = imageWidth / DisplayUtil.getScreenWidth(this)
        val sampleY = imageHeight / DisplayUtil.getScreenHeight(this)

        if (sampleY in 2 until sampleX) {
            sample = sampleX
        } else if (sampleX in 2 until sampleY) {
            sample = sampleY
        }

        options.inJustDecodeBounds = false
        options.inSampleSize = sample
        options.inPreferredConfig = Bitmap.Config.RGB_565

        return BitmapFactory.decodeResource(resources, musicPicRes, options)
    }

    private fun play() {
        optMusic(MusicService.ACTION_OPT_MUSIC_PLAY)
        startUpdateSeekBarProgress()
    }
    private fun pause() {
        optMusic(MusicService.ACTION_OPT_MUSIC_PAUSE);
        stopUpdateSeekBarProgree()
    }
    private fun next() {
        rootLayout.postDelayed(Runnable {
                optMusic(MusicService.ACTION_OPT_MUSIC_NEXT)
        },DiscView.DURATION_NEEDLE_ANIAMTOR.toLong())
        stopUpdateSeekBarProgree()
        tvTotalTime.text = duration2Time(0)
        tvCurrentTime.text = duration2Time(0)
    }
    private fun last(){
        rootLayout.postDelayed(Runnable {
        optMusic(MusicService.ACTION_OPT_MUSIC_LAST)
    },DiscView.DURATION_NEEDLE_ANIAMTOR.toLong())
    stopUpdateSeekBarProgree()
    tvTotalTime.text = duration2Time(0)
    tvCurrentTime.text = duration2Time(0)
    }

    private fun stop() {
        ivPlayOrPause.setImageResource(R.drawable.ic_play)
        musicSeekBar.progress = 0
        stopUpdateSeekBarProgree()
        tvTotalTime.text = duration2Time(0)
        tvCurrentTime.text = duration2Time(0)
    }

    private fun optMusic(action: String) {
        LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(action))
    }

    private fun initMusicDatas() {
        val musicData1 = MusicData(R.raw.music1, R.raw.ic_music1, "寻", "三亩地")
        val musicData2 = MusicData(R.raw.music2, R.raw.ic_music2, "Nightingale", "YANI")
        val musicData3 = MusicData(R.raw.music3, R.raw.ic_music3, "Cornfield Chase", "Hans Zimmer")

        mMusicDatas.add(musicData1)
        mMusicDatas.add(musicData2)
        mMusicDatas.add(musicData3)

        var intent = Intent(this, MusicService::class.java)
        intent.putExtra(PARAM_MUSIC_LIST, mMusicDatas)
        startService(intent)
    }
    private fun initMusicReceiver() {
        var intentFilter = IntentFilter()
        intentFilter.addAction(MusicService.ACTION_STATUS_MUSIC_PLAY)
        intentFilter.addAction(MusicService.ACTION_STATUS_MUSIC_PAUSE)
        intentFilter.addAction(MusicService.ACTION_STATUS_MUSIC_DURATION)
        intentFilter.addAction(MusicService.ACTION_STATUS_MUSIC_COMPLETE)
        /*注册本地广播*/
        LocalBroadcastManager.getInstance(this).registerReceiver(mMusicReceiver, intentFilter)
    }
    private fun startUpdateSeekBarProgress() {
        /*避免重复发送Message*/
        stopUpdateSeekBarProgree()
        mMusicHandler.sendEmptyMessageDelayed(0, 1000)
    }

    /*根据时长格式化称时间文本*/
    private fun duration2Time(duration: Int): String? {
        val min = duration / 1000 / 60
        val sec = duration / 1000 % 60
        return (if (min < 10) "0$min" else min.toString() + "") + ":" + if (sec < 10) "0$sec" else sec.toString() + ""
    }

    private fun updateMusicDurationInfo(totalDuration: Int) {
        musicSeekBar.progress = 0
        musicSeekBar.max = totalDuration
        tvTotalTime.text = duration2Time(totalDuration)
        tvCurrentTime.text = duration2Time(0)
        startUpdateSeekBarProgress()
    }
    private var mMusicHandler: Handler = object:Handler(){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            musicSeekBar.progress = musicSeekBar.progress+1000;
            tvCurrentTime.text = duration2Time(musicSeekBar.progress)
            startUpdateSeekBarProgress()
        }
    }
    private fun stopUpdateSeekBarProgree() {
        mMusicHandler.removeMessages(MUSIC_MESSAGE)
    }


    inner class MusicReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when(intent!!.action){
                MusicService.ACTION_STATUS_MUSIC_PLAY->{
                    val currentPosition = intent.getIntExtra(MusicService.PARAM_MUSIC_CURRENT_POSITION, 0)
                    ivPlayOrPause.setImageResource(R.drawable.ic_pause)
                    musicSeekBar.progress = currentPosition
                    if (!(discview as DiscView).isPlaying){
                        (discview as DiscView).playOrPause()
                    }
                }
                MusicService.ACTION_STATUS_MUSIC_PAUSE->{
                    ivPlayOrPause.setImageResource(R.drawable.ic_play)
                    if (!(discview as DiscView).isPlaying){
                        (discview as DiscView).playOrPause()
                    }
                }
                MusicService.ACTION_STATUS_MUSIC_DURATION->{
                    val duration = intent.getIntExtra(MusicService.PARAM_MUSIC_DURATION, 0)
                    updateMusicDurationInfo(duration)
                }
                MusicService.ACTION_STATUS_MUSIC_COMPLETE->{
                    complete(intent.getBooleanExtra(MusicService.PARAM_MUSIC_IS_OVER, true))
                }
            }

        }
    }

    private fun complete(isOver: Boolean) {
        if (isOver) {
            (discview as DiscView).stop()
        } else {
            (discview as DiscView).next()
        }
    }

    override fun onResume() {
        super.onResume()
        (discview as DiscView).setMusicDataList(mMusicDatas);
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMusicReceiver)
    }
}