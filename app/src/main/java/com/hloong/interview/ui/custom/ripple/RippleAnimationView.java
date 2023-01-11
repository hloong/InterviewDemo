package com.hloong.interview.ui.custom.ripple;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;

import com.hloong.interview.R;

import java.util.ArrayList;
import java.util.Collections;

public class RippleAnimationView extends RelativeLayout {

    public Paint paint;

    int rippleColor;
    int radius;
    private int strokWidth;
    private ArrayList<RippleCircleView> viewList = new ArrayList<>();
    private AnimatorSet animatorSet;
    private boolean animationRunning = false;

    public RippleAnimationView(Context context) {
        this(context,null);
    }

    public RippleAnimationView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RippleAnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        paint = new Paint();
        paint.setAntiAlias(true);//抗锯齿
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RippleAnimationView);
        int rippleType = typedArray.getInt(R.styleable.RippleAnimationView_ripple_anim_type, 0);
        radius= typedArray.getInteger(R.styleable.RippleAnimationView_radius, 54);
        strokWidth= typedArray.getInteger(R.styleable.RippleAnimationView_strokWidth, 2);
        rippleColor = typedArray.getColor(R.styleable.RippleAnimationView_ripple_anim_color, ContextCompat.getColor(context, R.color.rippleColor));
        paint.setStrokeWidth(UIUtils.getInstance(context).getWidth(strokWidth));
        if (rippleType == 0) {
            paint.setStyle(Paint.Style.FILL);
        } else {
            paint.setStyle(Paint.Style.STROKE);
        }
        paint.setColor(rippleColor);

        LayoutParams rippleParams = new LayoutParams(UIUtils.getInstance().getWidth(radius+strokWidth),
                UIUtils.getInstance().getWidth(radius+strokWidth));
        rippleParams.addRule(CENTER_IN_PARENT, TRUE);
        float maxScale = 10;
        int rippleDuration = 3000;
        int singleDelay = rippleDuration / 6;//间隔时间 （上一个波纹  和下一个波纹的）
        ArrayList<Animator> animatorList = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            RippleCircleView rippleCircleView = new RippleCircleView(this);
            addView(rippleCircleView,rippleParams);
            viewList.add(rippleCircleView);
            //x
            final ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(rippleCircleView,"ScaleX",1.0f,maxScale);
            scaleXAnimator.setRepeatCount(ObjectAnimator.INFINITE);//无限重复
            scaleXAnimator.setRepeatMode(ObjectAnimator.RESTART);
            scaleXAnimator.setStartDelay(i*singleDelay);
            scaleXAnimator.setDuration(rippleDuration);
            animatorList.add(scaleXAnimator);
            //Y
            final ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(rippleCircleView,"ScaleY",1.0f,maxScale);
            scaleYAnimator.setRepeatCount(ObjectAnimator.INFINITE);//无限重复
            scaleYAnimator.setRepeatMode(ObjectAnimator.RESTART);
            scaleYAnimator.setStartDelay(i*singleDelay);
            scaleYAnimator.setDuration(rippleDuration);
            animatorList.add(scaleYAnimator);
            //Alpha渐变
            final ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(rippleCircleView, "Alpha", 1.0f, 0f);
            alphaAnimator.setRepeatCount(ObjectAnimator.INFINITE);//无限重复
            alphaAnimator.setRepeatMode(ObjectAnimator.RESTART);
            alphaAnimator.setStartDelay(i * singleDelay);
            alphaAnimator.setDuration(rippleDuration);
            animatorList.add(alphaAnimator);
            //属性动画集合
            animatorSet = new AnimatorSet();
            animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
            animatorSet.playTogether(animatorList);

        }
    }

    public void startRippleAnimation(){
        if (!animationRunning){
            for (RippleCircleView rippleCircleView:viewList){
                rippleCircleView.setVisibility(VISIBLE);
            }
            animatorSet.start();
            animationRunning = true;
        }
    }
    public void stopRippleAnimation(){
        if (animationRunning){
            Collections.reverse(viewList);
            for (RippleCircleView rippleCircleView : viewList){
                rippleCircleView.setVisibility(INVISIBLE);
            }
            animatorSet.end();
            animationRunning = false;
        }
    }
    public boolean isAnimationRunning() {
        return animationRunning;
    }

    public int getStrokWidth(){
        return strokWidth;
    }
}
