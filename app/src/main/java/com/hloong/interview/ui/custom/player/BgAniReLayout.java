package com.hloong.interview.ui.custom.player;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;

import com.hloong.interview.R;

public class BgAniReLayout extends RelativeLayout {
    private LayerDrawable layerDrawable;
    private ObjectAnimator objectAnimator;
    public BgAniReLayout(Context context) {
        this(context,null);
    }

    public BgAniReLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BgAniReLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Drawable bgDrawable = getContext().getDrawable(R.drawable.ic_blackground);
        Drawable[] drawables = new Drawable[2];
        //初始化设置前景和背景一致
        drawables[0] = bgDrawable;
        drawables[1] = bgDrawable;
        layerDrawable = new LayerDrawable(drawables);

        objectAnimator = ObjectAnimator.ofFloat(this,"alpha",0f,1.0f);
        objectAnimator.setDuration(500);
        objectAnimator.setInterpolator(new AccelerateInterpolator());
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //0-1  (VSYNC - start )% duration
                int foregroundAlpha = (int)((float)animation.getAnimatedValue() * 255);
                layerDrawable.getDrawable(1).setAlpha(foregroundAlpha);
                setBackground(layerDrawable);
            }
        });
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                layerDrawable.setDrawable(0,layerDrawable.getDrawable(1));
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    public void setForeground(Drawable drawable){
        layerDrawable.setDrawable(1,drawable);
    }
}
