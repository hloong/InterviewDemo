package com.hloong.event;

import com.hloong.event.listener.OnClickListener;
import com.hloong.event.listener.OnTouchListener;

public class View {
    private int left;
    private int top;
    private int right;
    private int bottom;

    private OnTouchListener onTouchListener;
    private OnClickListener onClickListener;

    public View(){

    }
    public View(int left,int top,int right,int bottom){
        this.left = left;
        this.top  = top;
        this.right = right;
        this.bottom = bottom;
    }
    //判断View是否在点击范围内
    public boolean isContainer(int x,int y){
        if (x >= left && x <= right && y >= top && y <= bottom){
            return true;
        }
        return false;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public OnTouchListener getOnTouchListener() {
        return onTouchListener;
    }

    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

    public OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
    //接收分发的代码
    public boolean dispatchTouchEvent(MotionEvent event){
        boolean result = false;
        if (onTouchListener != null && onTouchListener.onTouch(this,event)){
            result = true;
        }
        if (!result && onTouchEvent(event)){
            result = true;
        }
        return result;
    }

    public boolean onTouchEvent(MotionEvent event){
        if (onClickListener != null){
            onClickListener.onClick(this);
            return true;
        }
        return false;
    }
}