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

    private int musicPicRes = -1;
    private final int DURATION_ANIMATION = 500;
    private final int INDEX_BACKGROUND = 0;
    private final int INDEX_FOREGROUND = 1;
    public BgAniReLayout(Context context) {
        this(context,null);
    }

    public BgAniReLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BgAniReLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayerDrawable();
        initObjectAnimator();
    }
    private void initLayerDrawable() {
        Drawable backgroundDrawable = getContext().getDrawable(R.drawable.ic_blackground);
        Drawable[] drawables = new Drawable[2];

        /*初始化时先将前景与背景颜色设为一致*/
        drawables[INDEX_BACKGROUND] = backgroundDrawable;
        drawables[INDEX_FOREGROUND] = backgroundDrawable;

        layerDrawable = new LayerDrawable(drawables);
    }
    private void initObjectAnimator() {
        objectAnimator = ObjectAnimator.ofFloat(this,"number",0f,1.0f);
        objectAnimator.setDuration(DURATION_ANIMATION);
        objectAnimator.setInterpolator(new AccelerateInterpolator());
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //0-1  (VSYNC - start )% duration
                int foregroundAlpha = (int)((float)animation.getAnimatedValue()*255);
                layerDrawable.getDrawable(INDEX_FOREGROUND).setAlpha(foregroundAlpha);
                setBackground(layerDrawable);
            }
        });
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                layerDrawable.setDrawable(INDEX_BACKGROUND,layerDrawable.getDrawable(INDEX_FOREGROUND));
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
        layerDrawable.setDrawable(INDEX_FOREGROUND,drawable);
    }

    public void beginAnimation(){
        objectAnimator.start();
    }
    public boolean isNeed2UpdateBackground(int musicPicRes){
        if (this.musicPicRes == -1) return true;
        if (musicPicRes != this.musicPicRes){
            return true;
        }
        return false;
    }
}
