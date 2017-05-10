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
    private int mRotation = -90;
    public FancyWatchView(Context context) {
        super(context);
    }

    public FancyWatchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FancyWatchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setmRotation(int rotation){
        mRotation = rotation;
        invalidate();
    }
    public int getmRotation(){
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


        //draw hour hand and minut hand
        Paint gradientPaint = new Paint();
        int[] mColors = new int[] {//渐变色数组
                0xFFFF0000, 0xFFFF00FF, 0xFF0000FF, 0xFF00FFFF, 0xFF00FF00,
                0xFFFFFF00, 0xFFFF0000
        };

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



//        gradientDrawable.draw(canvas);
//
//            Matrix matrix = new Matrix();
//            canvas.drawBitmap(bitmap, matrix, gradientPaint);

        canvas.save();

//        bitmap.recycle();
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

        int deltaDegree = 0;
        //check same area
        if(checkSameArea(vectorMove,vectorStart)){
            switch (checkArea(vectorMove)){
                case 1:
                    if((vectorMove.x/getVectorScale(vectorMove)-vectorStart.x/getVectorScale(vectorStart))>0){
                        deltaDegree = 1;
                    }else {
                        deltaDegree = -1;
                    }
                    break;
                case 2:
                    if((vectorMove.x/getVectorScale(vectorMove)-vectorStart.x/getVectorScale(vectorStart))<0){
                        deltaDegree = 1;
                    }else {
                        deltaDegree = -1;
                    }
                    break;
                case 3:
                    if((vectorMove.x/getVectorScale(vectorMove)-vectorStart.x/getVectorScale(vectorStart))<0){
                        deltaDegree = 1;
                    }else {
                        deltaDegree = -1;
                    }
                    break;
                case 4:
                    if((vectorMove.x/getVectorScale(vectorMove)-vectorStart.x/getVectorScale(vectorStart))>0){
                        deltaDegree = 1;
                    }else {
                        deltaDegree = -1;
                    }
                    break;
            }
        }
        //  (movePoint.x-centerPoint.x, movePoint.y-centerPoint.y)^2-(startPoint.x-centerPoint.x, startPoint.y-centerPoint.y)^2
//        float a = (movePoint.x-centerPoint.x)*(startPoint.x-centerPoint.x)+(movePoint.y-centerPoint.y)*(startPoint.y-centerPoint.y);
//        double b = Math.sqrt((movePoint.x-centerPoint.x)*(movePoint.x-centerPoint.x)+(movePoint.y-centerPoint.y)*(movePoint.y-centerPoint.y))*Math.sqrt((startPoint.x-centerPoint.x)*(startPoint.x-centerPoint.x)+(startPoint.y-centerPoint.y)*(startPoint.y-centerPoint.y));
        Log.d("math", deltaDegree+"");
        int mRotation = getmRotation();
        setmRotation((-deltaDegree)*1+mRotation);
    }

    private float getVectorScale(PointF vector) {
        return (float) Math.sqrt(vector.x*vector.x+vector.y*vector.y);

    }

    private int checkArea(PointF vectorMove) {
        if(vectorMove.x>0){
            if(vectorMove.y>0){
                return 1;
            }else {
                return 2;
            }
        }else {
            if(vectorMove.y>0){
                return 4;
            }else {
                return 3;
            }
        }
    }

    private boolean checkSameArea(PointF vectorMove, PointF vectorStart) {
        if(vectorMove.x*vectorStart.x>0&&vectorMove.y*vectorStart.y>0){
            return true;
        }
        return false;
    }
}
