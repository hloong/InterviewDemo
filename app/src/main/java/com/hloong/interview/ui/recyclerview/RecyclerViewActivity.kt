package com.hloong.interview.ui.recyclerview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.bumptech.glide.Glide
import com.hloong.interview.R
import kotlinx.android.synthetic.main.activity_recycler_view.*


class RecyclerViewActivity : AppCompatActivity() {
    private var mSuspensionHeight = 0
    private var mCurrentPosition = 0
    private var layoutManager = LinearLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = FeedAdapter()
        recyclerView.setHasFixedSize(true)
        recyclerView.addOnScrollListener(object: OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                mSuspensionHeight = suspension_bar.height//获取悬浮条的高度
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                var view = layoutManager.findViewByPosition(mCurrentPosition + 1)
                if (view != null){
                    if (view.top  <= mSuspensionHeight){
                        suspension_bar.scrollY = -(mSuspensionHeight-view.top)
                    }else{
                        suspension_bar.scrollY = 0
                    }
                }
                if (mCurrentPosition != layoutManager.findFirstVisibleItemPosition()){
                    mCurrentPosition = layoutManager.findFirstVisibleItemPosition()
                    updateSuspensionBar()
                }
            }
        })

        updateSuspensionBar()
    }

    private fun updateSuspensionBar() {
        Glide.with(this)
            .load(getAvatarResId(mCurrentPosition))
            .centerInside()
            .into(iv_avatar)
        tv_nickname!!.text = "HLoong $mCurrentPosition"
    }

    private fun getAvatarResId(position: Int): Int {
        when (position % 4) {
            0 -> return R.drawable.avatar1
            1 -> return R.drawable.avatar2
            2 -> return R.drawable.avatar3
            3 -> return R.drawable.avatar4
        }
        return 0
    }

}