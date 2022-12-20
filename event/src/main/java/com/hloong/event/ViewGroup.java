package com.hloong.event;

import java.util.ArrayList;
import java.util.List;

public class ViewGroup extends View{
    List<View> childList = new ArrayList<>();
    private View[] mChildren = new View[0];
    public void addView(View view){
        if(view == null){
            return;
        }
        childList.add(view);
        mChildren = childList.toArray(new View[childList.size()]);
    }

    public boolean dispatchTouchEvent(MotionEvent ev){
        boolean intercepted = onInterceptTouchEvent(ev);
        //大部分处理逻辑都在处理action down事件
        int actionMasked = ev.getActionMasked();
        if (actionMasked != MotionEvent.ACTION_CANCEL && !intercepted){
            if (actionMasked == MotionEvent.ACTION_DOWN){
                final View[] children = mChildren;
                for (int i = children.length-1; i >=0 ; i--) {//为啥从后面开始遍历，主要是最后的View来处理点击事件的概率比较大
                    View child = children[i];
                    //view能接收到事件
                    if (!child.isContainer(ev.getX(),ev.getY())){
                        continue;
                    }
                    //能接收事件 child 分发
                    if (dispatchTransformedTouchEvent(ev,child)){

                    }

                }
            }
        }

        return intercepted;
    }
    //分发处理 子控件View
    private boolean dispatchTransformedTouchEvent(MotionEvent event,View child){
        final boolean handled;
        handled = child.dispatchTouchEvent(event);
        return handled;
    }

    public boolean onInterceptTouchEvent(MotionEvent ev){
        return false;
    }


}
