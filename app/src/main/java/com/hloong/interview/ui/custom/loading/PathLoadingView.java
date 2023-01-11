package com.hloong.interview.ui.custom.loading;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class PathLoadingView extends View {

    private Paint paint;
    private Path path;
    private PathMeasure pathMeasure;
    private Path dst;
    private float length;
    private float mAnimatorValue;
    public PathLoadingView(Context context) {
        super(context);
    }

    public PathLoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public PathLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(10f);
        paint.setStyle(Paint.Style.STROKE);

        path = new Path();
        path.addCircle(300f,300f,100f,Path.Direction.CW);
        //PathMeasure 相当于Path的测量工具类
        pathMeasure = new PathMeasure(path,false);
        length = pathMeasure.getLength();
        //        0<distance<length*百分比
        dst = new Path();
        //设置动画过程的监听
        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0,1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatorValue = (float)animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.setDuration(1000);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        dst.reset();
        float distance = length * mAnimatorValue;
        float start = (float)(distance - ((0.5-Math.abs(mAnimatorValue-0.5))*length));
        pathMeasure.getSegment(start,distance,dst,true);
        canvas.drawPath(dst,paint);
    }
}
