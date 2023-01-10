package com.hloong.interview.ui.custom.car_ani;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.hloong.interview.R;

public class CarView extends View {
    private Bitmap carBitmap;
    private Path path;
    private PathMeasure pathMeasure;
    private float distanceRatio = 0;
    private Paint circlePaint;
    private Paint carPaint;
    private Matrix carMatrix;

    public CarView(Context context) {
        super(context);
        init();
    }

    public CarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(){
        carBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_car);
        path = new Path();
        path.addCircle(0,0,200,Path.Direction.CW);
        pathMeasure = new PathMeasure(path,false);

        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.BLACK);
        circlePaint.setStrokeWidth(10f);
        circlePaint.setStyle(Paint.Style.STROKE);

        carPaint = new Paint();
        carPaint.setAntiAlias(true);
        carPaint.setStrokeWidth(2f);
        carPaint.setStyle(Paint.Style.STROKE);

        carMatrix = new Matrix();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        int w = canvas.getWidth();
        int h = canvas.getHeight();

        canvas.translate(w/2,h/2);//移到中心点
        carMatrix.reset();

        distanceRatio+= 0.001f;
        if (distanceRatio >= 1){
            distanceRatio = 0;
        }
        float[] pos = new float[2];
        float[] tan = new float[2];
        float distance = pathMeasure.getLength()*distanceRatio;
        pathMeasure.getPosTan(distance,pos,tan);
        float degree = (float)(Math.atan2(tan[1],tan[0])* 180 /Math.PI); //计算小车要旋转的角度
        carMatrix.postRotate(degree,carBitmap.getWidth()/2,carBitmap.getHeight()/2);
        carMatrix.postTranslate(pos[0]-carBitmap.getWidth()/2,pos[1]-carBitmap.getHeight()/2);
        canvas.drawPath(path,circlePaint);
        canvas.drawBitmap(carBitmap,carMatrix,carPaint);
        invalidate();
    }
}
