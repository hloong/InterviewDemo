package com.hloong.interview.ui.custom.vlayout

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.GridLayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.bumptech.glide.Glide
import com.hloong.interview.R
import com.sunfusheng.marqueeview.MarqueeView
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import kotlinx.android.synthetic.main.activity_v_layout.*


class VLayoutActivity : AppCompatActivity() {
    //应用
    var ITEM_NAMES =
        arrayOf("天猫", "聚划算", "天猫国际", "外卖", "天猫超市", "充值中心", "飞猪旅行", "领金币", "拍卖", "分类")
    var IMG_URLS = intArrayOf(
        R.mipmap.ic_tian_mao,
        R.mipmap.ic_ju_hua_suan,
        R.mipmap.ic_tian_mao_guoji,
        R.mipmap.ic_waimai,
        R.mipmap.ic_chaoshi,
        R.mipmap.ic_voucher_center,
        R.mipmap.ic_travel,
        R.mipmap.ic_tao_gold,
        R.mipmap.ic_auction,
        R.mipmap.ic_classify
    )
    //    高颜值商品位
    var ITEM_URL =
        intArrayOf(R.mipmap.item1, R.mipmap.item2, R.mipmap.item3, R.mipmap.item4, R.mipmap.item5)
    var GRID_URL = intArrayOf(
        R.mipmap.flashsale1,
        R.mipmap.flashsale2,
        R.mipmap.flashsale3,
        R.mipmap.flashsale4
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_v_layout)
        initView()
    }


    private fun initView() {
        var vlayoutManager = VirtualLayoutManager(this)
        recycler.layoutManager = vlayoutManager
        var viewPool = RecyclerView.RecycledViewPool()
        recycler.setRecycledViewPool(viewPool)
        viewPool.setMaxRecycledViews(0,10)

        val bannerAdapter: BaseDelegeteAdapter = object : BaseDelegeteAdapter(
            this
            , LinearLayoutHelper(), R.layout.vlayout_banner, 1
        ) {
            override fun onBindViewHolder(holder: BaseViewHolder, i: Int) {
                val arrayList: ArrayList<String> = ArrayList()
                arrayList.add("https://img.zcool.cn/community/017a9460d52b8311013f4720c83ccf.jpg@1280w_1l_2o_100sh.jpg")
                arrayList.add("https://img.zcool.cn/community/013d9760331be111013ef90f24aa2f.jpg@1280w_1l_2o_100sh.jpg")
                arrayList.add("https://img.zcool.cn/community/0141145ef9b9b4a801215aa00e459e.jpg@2o.jpg")
                // 绑定数据
                var mBanner = holder.getView(R.id.banner) as Banner
                //设置banner样式

                mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                //设置图片加载器
                mBanner.setImageLoader(GlideImageLoader())
                //设置图片集合
                mBanner.setImages(arrayList)
                //设置banner动画效果
                mBanner.setBannerAnimation(Transformer.DepthPage)
                //设置标题集合（当banner样式有显示title时）
                //        mBanner.setBannerTitles(titles);
                //设置自动轮播，默认为true
                mBanner.isAutoPlay(true)
                //设置轮播时间
                mBanner.setDelayTime(3000)
                //设置指示器位置（当banner模式中有指示器时）
                mBanner.setIndicatorGravity(BannerConfig.CENTER)
                //banner设置方法全部调用完毕时最后调用
                mBanner.start()
                mBanner.setOnBannerListener {
                    Toast.makeText(
                        applicationContext,
                        "banner点击了$it",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                super.onBindViewHolder(holder, i)
            }
        }
        val gridLayoutHelper = GridLayoutHelper(5)
        gridLayoutHelper.setPadding(0, 16, 0, 0)
        gridLayoutHelper.vGap = 10
        gridLayoutHelper.hGap = 0 //// 控制子元素之间的水平间距


        val menuAdapter: BaseDelegeteAdapter =
            object : BaseDelegeteAdapter(this, gridLayoutHelper, R.layout.vlayout_menu, 10) {
                override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                    holder.setText(
                        R.id.tv_menu_title_home,
                        ITEM_NAMES[position] + ""
                    )
                    holder.setImageResource(R.id.iv_menu_home, IMG_URLS[position])
                    holder.getView<View>(R.id.ll_menu_home)
                        .setOnClickListener {
                            Toast.makeText(
                                applicationContext,
                                ITEM_NAMES[position],
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }

        val newsAdapter: BaseDelegeteAdapter = object : BaseDelegeteAdapter(
            this, LinearLayoutHelper(),
            R.layout.vlayout_news, 1
        ) {
            override fun onBindViewHolder(holder: BaseViewHolder, i: Int) {
                val marqueeView1 = holder.getView<MarqueeView>(R.id.marqueeView1)
                val marqueeView2 = holder.getView<MarqueeView>(R.id.marqueeView2)
                val info1: MutableList<String> = ArrayList()
                info1.add("天猫超市最近发大活动啦，快来抢")
                info1.add("没有最便宜，只有更便宜！")
                val info2: MutableList<String> = ArrayList()
                info2.add("这个是用来搞笑的，不要在意这写小细节！")
                info2.add("啦啦啦啦，我就是来搞笑的！")
                marqueeView1.startWithList(info1)
                marqueeView2.startWithList(info2)
                // 在代码里设置自己的动画
                marqueeView1.startWithList(info1, R.anim.anim_bottom_in, R.anim.anim_top_out)
                marqueeView2.startWithList(info2, R.anim.anim_bottom_in, R.anim.anim_top_out)
                marqueeView1.setOnItemClickListener { position, textView ->
                    Toast.makeText(
                        applicationContext,
                        textView.text.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                marqueeView2.setOnItemClickListener { position, textView ->
                    Toast.makeText(
                        applicationContext,
                        textView.text.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        var delegateAdapter = DelegateAdapter(vlayoutManager, true)
        delegateAdapter.addAdapter(bannerAdapter)
        delegateAdapter.addAdapter(menuAdapter)
        delegateAdapter.addAdapter(newsAdapter)
        for (i in 0 until ITEM_URL.size) {
            val titleAdapter: BaseDelegeteAdapter = object : BaseDelegeteAdapter(
                this,
                LinearLayoutHelper(), R.layout.vlayout_title, 1
            ) {
                override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                    super.onBindViewHolder(holder, position)
                    holder.setImageResource(R.id.iv, ITEM_URL[i])
                }
            }
            val gridHelper = GridLayoutHelper(2)
            val gridAdapter: BaseDelegeteAdapter = object : BaseDelegeteAdapter(
                this, gridHelper,
                R.layout.vlayout_grid, 4
            ) {
                override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                    val item = GRID_URL[position]
                    val iv: ImageView = holder.getView(R.id.iv)
                    Glide.with(applicationContext).load(item).into(iv)
                    iv.setOnClickListener {
                        Toast.makeText(
                        applicationContext,
                        "item$position",
                        Toast.LENGTH_SHORT
                    ).show() }
                }
            }
            delegateAdapter.addAdapter(titleAdapter)
            delegateAdapter.addAdapter(gridAdapter)
        }

        recycler!!.adapter = delegateAdapter
    }
}