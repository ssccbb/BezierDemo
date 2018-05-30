package com.sung.bezierdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
* 用于演示贝塞尔曲线的绘制过程view
* */
public class BezierView extends View {
    public static final String TAG = BezierView.class.getSimpleName();
    private int mPointsNumber = -1;

    private Paint point_panit = new Paint();

    public BezierView(Context context) {
        super(context);
    }

    public BezierView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BezierView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    /**
     * 清空画面
     * */
    public void clear(){

    }

    /**
     * 设置点数量
     * */
    public void setmPointsNumber(int mPointsNumber) {
        this.mPointsNumber = mPointsNumber;
        clear();
    }
}
