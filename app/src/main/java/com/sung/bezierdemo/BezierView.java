package com.sung.bezierdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于演示贝塞尔曲线的绘制过程view
 */
public class BezierView extends View {
    public static final String TAG = BezierView.class.getSimpleName();
    private int mPointsNumber = 3;

    private Paint point_panit;
    private Paint line_panit;
    private Paint bezier_panit;

    private boolean isPaintting = false;

    private float touchX = 0;
    private float touchY = 0;
    private double scale = -1;

    private List<List<float[]>> points = new ArrayList<>();
    private List<float[]> bezier = new ArrayList<>();

    private Handler pointHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                paintDataInit(msg);
            }catch (IndexOutOfBoundsException ex){
                Log.e(TAG, "handleMessage: IndexOutOfBoundsException\n"+ex.toString() );
            }
        }
    };

    public BezierView(Context context) {
        super(context);
        init();
    }

    public BezierView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BezierView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (bezier_panit == null) {
            bezier_panit = new Paint();
            bezier_panit.setAntiAlias(true);
            bezier_panit.setColor(Color.RED);
        }
        if (point_panit == null) {
            point_panit = new Paint();
            point_panit.setAntiAlias(true);
            point_panit.setColor(Color.BLACK);
        }
        if (line_panit == null) {
            line_panit = new Paint();
            line_panit.setAntiAlias(true);
            line_panit.setColor(Color.parseColor("#20000000"));
            line_panit.setStrokeWidth(5);
        }

        for (int i = 0; i < mPointsNumber; i++) {
            points.add(new ArrayList<float[]>());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (List<float[]> list : points) {
            for (int i = 0; i < list.size(); i++) {
                //draw points
                float[] currentPoint = list.get(i);
                canvas.drawCircle(currentPoint[0], currentPoint[1], 10, point_panit);
                //draw lines
                if (i == 0) continue;
                float[] lastPoint = list.get(i - 1);
                canvas.drawLine(lastPoint[0], lastPoint[1], currentPoint[0], currentPoint[1], line_panit);
            }
        }

        //bezier
        for (float[] points : bezier) {
            canvas.drawCircle(points[0],points[1],10,bezier_panit);
        }
        super.onDraw(canvas);
    }

    /**
     * 用于PointHandler 动态初始化所有的点 提供绘制数据
     * */
    private void paintDataInit(Message msg){
        //第一条线段上动态点到初始点距离
        double length = (double) msg.obj;
        //第一条线段长度
        double firstLineLength = getLineLength(points.get(0).get(0), points.get(0).get(1));
        //贝塞尔比率
        scale = length / firstLineLength;
        //动态点坐标
        float[] firstPoint = getPointInLine(length, points.get(0).get(0), points.get(0).get(1));

        //初始化点集合
        for (int i = 0; i < points.size(); i++) {
            //顶层点集合已经在onTouchEvent处理
            if (i == 0) continue;
            List<float[]> last = points.get(i - 1);
            //下一层点集合大小
            int listSize = last.size() - 1;
            //后续层级点都需要动态变化 添加之前清空
            List<float[]> next = points.get(i);
            next.clear();
            //点填充
            for (int j = 0; j < listSize; j++) {
                if (i == 1 && j == 0) {//从第二层首个点（该点作为标志点）
                    next.add(firstPoint);
                } else {//按scale计算下一个点
                    float[] startPoint = last.get(j);
                    float[] endPoint = last.get(j + 1);
                    double lineLength = getLineLength(startPoint, endPoint);
                    double nextLength = lineLength * scale;
                    next.add(getPointInLine(nextLength, startPoint, endPoint));

                    //线轨迹
                    if (i==(points.size()-1) && j==0) bezier.add(next.get(0));
                }
            }
        }

        //首段动态线段到达峰值暂停绘制
        if (length > firstLineLength) {
            isPaintting = false;
            return;
        }
        length++;
        Message message = new Message();
        message.obj = length;
        postInvalidate();
        pointHandler.sendMessage(message);
    }

    /**
     * 获取线段长度 d**2=(x1-x2)**2+(y1-y2)**2
     */
    private double getLineLength(float[] startPoint, float[] endPoint) {
        float startX = startPoint[0];
        float startY = startPoint[1];
        float endX = endPoint[0];
        float endY = endPoint[1];
        return Math.sqrt((startX - endX) * (startX - endX) + (startY - endY) * (startY - endY));
    }

    /**
     * 根据x获取线段上点的坐标 y=kx+b
     */
    private float[] getPointInLine(float x, float[] startPoint, float[] endPoint) {
        float startX = startPoint[0];
        float startY = startPoint[1];
        float endX = endPoint[0];
        float endY = endPoint[1];

        //保证当前选择的点一定在线段上
        if (startX < endX) {
            if (x < startX || x > endX) return null;
        } else if (startX == endX) {
            return null;
        } else {
            if (x > startX || x < endX) return null;
        }

        // y=kx+b
        float k = (startY - endY) / (startX - endX);
        float b = (startY * endX - endY * startX) / (endX - startX);
        float y = k * x + b;
        float[] point = {x, y};
        return point;
    }

    /**
     * 根据长度获取线段上点的坐标
     */
    private float[] getPointInLine(double length, float[] startPoint, float[] endPoint) {
        float startX = startPoint[0];
        float startY = startPoint[1];
        float endX = endPoint[0];
        float endY = endPoint[1];

        double d = Math.sqrt((startX - endX) * (startX - endX) + (startY - endY) * (startY - endY));
        double scale = d / length;

        float x = Math.abs(startX - endX);
        float y = Math.abs(startY - endY);

        //用比例计算x，y坐标（直角三角形）
        x = (float) ((startX <= endX) ? (startX + x / scale) : (startX - x / scale));
        y = (float) ((startY <= endY) ? (startY + y / scale) : (startY - y / scale));

        float[] point = {x, y};
        return point;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isPaintting) return false;
                touchX = event.getX();
                touchY = event.getY();
                if (points.get(0) != null && points.get(0).size() < mPointsNumber) {
                    float[] point = {touchX, touchY};
                    points.get(0).add(point);
                }
                postInvalidate();
                start();
                break;
        }
        return false;
    }

    /**
     * 开始绘制
     * */
    private void start() {
        if (points.get(0).size() != mPointsNumber) return;
        Message msg = new Message();
        msg.obj = (double) 1;
        isPaintting = true;
        pointHandler.sendMessage(msg);
    }

    /**
     * 清空画面
     */
    public void clear() {
        for (List<float[]> list : points) {
            list.clear();//依次清层级的点 不能直接清集合
        }
        //清线
        bezier.clear();
        postInvalidate();
    }

    /**
     * 设置点数量
     */
    public void setmPointsNumber(int mPointsNumber) {
        this.mPointsNumber = mPointsNumber;
        points.clear();
        for (int i = 0; i < this.mPointsNumber; i++) {
            points.add(new ArrayList<float[]>());
        }
        clear();
    }
}
