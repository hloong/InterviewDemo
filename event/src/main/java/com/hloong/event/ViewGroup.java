package com.hloong.event;

import java.util.ArrayList;
import java.util.List;

public class ViewGroup extends View{
    List<View> childList = new ArrayList<>();
    private View[] mChildren = new View[0];
    public ViewGroup(int left, int top, int right, int bottom) {
        super(left, top, right, bottom);
    }
    public void addView(View view){
        if(view == null){
            return;
        }
        childList.add(view);
        mChildren = childList.toArray(new View[childList.size()]);
    }
    private TouchTarget firstTouchTarget;
    public boolean dispatchTouchEvent(MotionEvent ev){
        boolean intercepted = onInterceptTouchEvent(ev);
        boolean handled = false;
        TouchTarget newTouchTarget = null;
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
                        handled = true;
                        newTouchTarget = addTouchTarget(child);
                        break;
                    }

                }
            }
            //当前ViewGroup dispatchTransformedTouchEvent
            if (firstTouchTarget == null){
                handled = dispatchTransformedTouchEvent(ev,null);
            }
        }
        return handled;
    }
    private TouchTarget addTouchTarget(View child){
        final TouchTarget target = TouchTarget.obtain(child);
        target.next = firstTouchTarget;
        firstTouchTarget = target;
        return target;
    }
    //参考手写RecyclerView 回收池策略
    private static final class TouchTarget{
        public View child;
        private static TouchTarget sRecycleBin;
        private static final Object sRecycleLock = new Object[0];
        public TouchTarget next;
        private static int sRecycleCount;
        public static TouchTarget obtain(View child){
            TouchTarget target;
            synchronized (sRecycleLock){
                if (sRecycleLock == null){
                    target = new TouchTarget();
                }else {
                    target = sRecycleBin;
                }
                sRecycleBin = target.next;
                sRecycleCount--;
                target.child = null;
            }
            target.child = child;
            return target;
        }
        public void recycle(){
            if (child == null){
                throw new IllegalStateException("already recycled");
            }
            synchronized (sRecycleLock){
                if (sRecycleCount < 32){
                    next = sRecycleBin;
                    sRecycleBin = this;
                    sRecycleCount += 1;
                }
            }

        }
    }
    //分发处理 子控件View
    private boolean dispatchTransformedTouchEvent(MotionEvent event,View child){
        boolean handled = false;
        if (child != null){
            handled = child.dispatchTouchEvent(event);
        }else {
            handled = super.dispatchTouchEvent(event);
        }
        return handled;
    }

    public boolean onInterceptTouchEvent(MotionEvent ev){
        return false;
    }

    private String name;

    @Override
    public String toString() {
        return ""+name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
