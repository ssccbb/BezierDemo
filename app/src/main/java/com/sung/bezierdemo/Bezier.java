package com.sung.bezierdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class Bezier extends View {
    private Paint paint;
    private Paint paint1;
    private Path path;

    private List<Float> startPoint,endPoint,controlPoint;

    public Bezier(Context context) {
        super(context);
        init();
    }

    public Bezier(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        path = new Path();
        paint = new Paint();
        paint1 = new Paint();

        paint.setAntiAlias(true);
        paint.setColor(Color.RED);

        paint1.setAntiAlias(true);
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setColor(Color.RED);
        paint1.setStrokeWidth(2);

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (startPoint!=null){
            canvas.drawCircle(startPoint.get(0),startPoint.get(1),5,paint);
        }
        if (endPoint!=null){
            canvas.drawCircle(endPoint.get(0),endPoint.get(1),5,paint);
        }
        if (controlPoint!=null){
            canvas.drawCircle(controlPoint.get(0),controlPoint.get(1),5,paint);
        }
        if (startPoint!=null&endPoint!=null&controlPoint!=null){
            path.moveTo(startPoint.get(0),startPoint.get(1));
            path.quadTo(controlPoint.get(0),controlPoint.get(1),endPoint.get(0),endPoint.get(1));
            canvas.drawPath(path,paint1);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (startPoint == null){
                    startPoint = new ArrayList<>();
                    startPoint.add(event.getX());
                    startPoint.add(event.getY());
                    break;
                }
                if (endPoint == null){
                    endPoint = new ArrayList<>();
                    endPoint.add(event.getX());
                    endPoint.add(event.getY());
                    break;
                }
                if (controlPoint == null){
                    controlPoint = new ArrayList<>();
                    controlPoint.add(event.getX());
                    controlPoint.add(event.getY());
                    break;
                }
                clearCavans();
                break;
        }
        postInvalidate();
        return false;
    }

    public void clearCavans(){
        path.reset();
        startPoint.clear();
        endPoint.clear();
        controlPoint.clear();
        startPoint = null;
        endPoint = null;
        controlPoint = null;
    }
}
