package com.hloong.event.listener;

import com.hloong.event.MotionEvent;
import com.hloong.event.View;

public interface OnTouchListener {
    boolean onTouch(View v, MotionEvent event);
}
