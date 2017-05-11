package cn.beyace.www.fancywatch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by huangzilong on 2017/5/10.
 */

public class FancyWatchView extends View{
    private float mRotation = -90;
    public FancyWatchView(Context context) {
        super(context);
    }

    public FancyWatchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FancyWatchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setmRotation(float rotation){
        mRotation = rotation;
        invalidate();
    }
    public float getmRotation(){
        return mRotation;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(200,200);

        //draw hardware
        Paint hardWare = new Paint();
        hardWare.setColor(Color.DKGRAY);
        hardWare.setAntiAlias(true);

        //draw number dots
        for (int i =0 ; i<12 ; i++){
            canvas.drawCircle(100,0,3,hardWare);
            canvas.rotate(30);
        }

        canvas.drawCircle(0,0,60,hardWare);
        hardWare.setStyle(Paint.Style.STROKE);
        hardWare.setStrokeWidth(10);
        canvas.drawCircle(0,0,140,hardWare);


        //draw hour hand and minute hand
        Paint gradientPaint = new Paint();
//        int[] mColors = new int[] {//渐变色数组
//                0xFFFF0000, 0xFFFF00FF, 0xFF0000FF, 0xFF00FFFF, 0xFF00FF00,
//                0xFFFFFF00, 0xFFFF0000
//        };

        int[] mYColors = new int[]{
                0x00DED34C,  0xAFDED34C
        };
        int[] mGColors = new int[]{
                0x001CB0D2,  0xAF1CB0D2
        };

        float[] mGPositions = new float[]{
                0f, 1f
        };

        Paint linePaint = new Paint();
        linePaint.setStrokeWidth(5);
        linePaint.setStyle(Paint.Style.STROKE);

        Path circlePath = new Path();
        circlePath.addCircle(0,0,100, Path.Direction.CW);



        SweepGradient sweepGradient = new SweepGradient(0,0,mYColors,mGPositions);
        SweepGradient sweepGradient1 = new SweepGradient(0,0,mGColors,mGPositions);

        gradientPaint.setStyle(Paint.Style.STROKE);
        gradientPaint.setAntiAlias(true);
        gradientPaint.setStrokeWidth(80);

        gradientPaint.setShader(sweepGradient1);

        canvas.save();
        canvas.rotate(-90+(mRotation+90)/12);
        canvas.drawPath(circlePath,gradientPaint);
//        linePaint.setColor(Color.parseColor("#1CB0D2"));
//        canvas.drawLine(50,0,150,0,linePaint);

        canvas.restore();

        canvas.rotate(mRotation);
        gradientPaint.setShader(sweepGradient);
        canvas.drawPath(circlePath,gradientPaint);
        linePaint.setColor(Color.parseColor("#DED34C"));
        canvas.drawLine(60,0,140,0,linePaint);

        canvas.save();

    }

    PointF startPoint;
    PointF movePoint;

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                startPoint = new PointF(motionEvent.getX(), motionEvent.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                if(movePoint == null){
                    movePoint = new PointF(motionEvent.getX(), motionEvent.getY());
                }else{
                    movePoint.set(motionEvent.getX(),motionEvent.getY());
                }
                calculateAngle();
                startPoint.set(motionEvent.getX(), motionEvent.getY());
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    private void calculateAngle() {
        PointF centerPoint = new PointF(200,200);
        PointF vectorStart = new PointF(startPoint.x-centerPoint.x, startPoint.y - centerPoint.y);
        PointF vectorMove = new PointF(movePoint.x-centerPoint.x, movePoint.y-centerPoint.y);

        float mRotation = getmRotation();
        double k1 = caculateK(vectorStart);
        double k2 = caculateK(vectorMove);
        double tan = (k1-k2)/(1+k1*k2);
//        Log.d("tan", tan+"");
        double degree = Math.toDegrees(tan);
//        Log.d("degree", degree+"");
        setmRotation(mRotation-(float)degree);
    }

    private double caculateK(PointF vector) {
        double k = vector.y/vector.x;
        return k;
    }


    //set color
    //add countDown
    
}
