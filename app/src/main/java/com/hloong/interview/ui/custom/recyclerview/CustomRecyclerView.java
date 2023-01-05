package com.hloong.interview.ui.custom.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.hloong.interview.R;

import java.util.ArrayList;
import java.util.List;

public class CustomRecyclerView extends ViewGroup {
    private Adapter adapter;
    //当前显示的View
    private List<View> viewList;
    //当前滑动的y值
    private int currentY;
    //行数
    private int rowCount;
    //view的第一行  是占内容的几行
    private int firstRow;
    //y偏移量
    private int scrollY;
    //初始化  第一屏最慢
    private boolean needRelayout;
    private int width;

    private int height;
    private int[] heights;//item  高度
    Recycler recycler;
    //最小滑动距离
    private int touchSlop;
    public Adapter getAdapter() {
        return adapter;
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
        if (adapter != null){
            recycler = new Recycler(adapter.getViewTypeCount());
            scrollY = 0;
            firstRow = 0;
            needRelayout = true;
            requestLayout();//1  onMeasure   2  onLayout
        }
    }

    public CustomRecyclerView(Context context) {
        super(context);
        init(context,null);
    }

    public CustomRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }


    public CustomRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs) {
        ViewConfiguration configuration = ViewConfiguration.get(context);
        this.touchSlop=configuration.getScaledTouchSlop();
        this.viewList = new ArrayList<>();
        this.needRelayout = true;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int h = 0;
        if (adapter != null) {
            this.rowCount = adapter.getCount();
            heights = new int[rowCount];
            for (int i = 0; i < heights.length; i++) {
                heights[i] = adapter.getHeight(i);
            }
        }
        //数据的高度
        int tmpH  = sumArray(heights, 0, heights.length);
        h= Math.min(heightSize, tmpH);
        setMeasuredDimension(widthSize, h);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    //    firstIndex  firstIndex+count
    private int sumArray(int array[], int firstIndex, int count) {
        int sum = 0;
        count += firstIndex;
        for (int i = firstIndex; i < count; i++) {
            sum += array[i];
        }
        return sum;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (needRelayout || changed) {
            needRelayout = false;

            viewList.clear();
            removeAllViews();
            if (adapter != null) {
                //摆放
                width = r - l;
                height = b - t;
                int left, top = 0, right, bottom;
                for (int i = 0; i < rowCount&&top<height; i++) {
                    right = width;
                    bottom = top + heights[i];
//                    生成一个View
                    View view= makeAndStep(i, 0, top, width, bottom);
                    viewList.add(view);
                    top = bottom;//循环摆放
                }
            }
        }
    }
    private View makeAndStep(int row, int left, int top, int right, int bottom) {
        View view = obtainView(row, right - left, bottom - top);
        view.layout(left, top, right, bottom);
        return view;
    }
    private View obtainView(int row, int width, int height) {
//        key type
        int itemType= adapter.getItemViewType(row);
//       取不到
        View reclyView = recycler.get(itemType);
        View view = null;
        if (reclyView == null) {
            view = adapter.onCreateViewHolder(row, reclyView, this);
            if (view == null) {
                throw new RuntimeException("onCreateViewHodler  必须填充布局");
            }
        }else {
            view = adapter.onBinderViewHolder(row, reclyView, this);
        }
        view.setTag(R.id.tag_type_view, itemType);
        view.measure(MeasureSpec.makeMeasureSpec(width,MeasureSpec.EXACTLY)
                ,MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY));
        addView(view,0 );
        return view;
    }
    @Override
    public void scrollBy(int x, int y) {
        super.scrollBy(x, y);
    }

    interface Adapter{
        View onCreateViewHolder(int pos,View convertView,ViewGroup parent);
        View onBinderViewHolder(int pos,View convertView,ViewGroup parent);
        int getItemViewType(int row);
        int getViewTypeCount();
        int getCount();
        public int getHeight(int index);
    }
}
