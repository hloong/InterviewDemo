package com.hloong.interview.ui.custom.ripple;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class RippleCircleView extends View {

    private RippleAnimationView rippleAnimationView;

    public RippleCircleView(RippleAnimationView rippleAnimationView){
        this(rippleAnimationView.getContext(),null);
        this.rippleAnimationView = rippleAnimationView;
    }

    public RippleCircleView(Context context) {
        super(context);
    }

    public RippleCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RippleCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int radius = (Math.min(getWidth(),getHeight()))/2;
        canvas.drawCircle(radius, radius, radius - rippleAnimationView.getStrokWidth(), rippleAnimationView.paint);
    }
}
