package com.guozhe.android.sensor;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by guozhe on 2017. 9. 1..
 */

public class Graph extends View {
    private int realWith,realHeight;
    private final int xDensity = 100 ,yDensity = 100;
    private int cellHeight,cellWith;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final int DEFAULT_GRAPH_COLOR = Color.CYAN;
    private int lineWidht = 10;
    private Point zeroByZero = new Point();
    private List<Integer> points = new ArrayList<>();
    private float[] lines = new float[xDensity*2];


    public Graph(Context context) {
        super(context);
    }

    public Graph(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.Graph);
        int graphColor = ta.getInt(R.styleable.Graph_graphColor,DEFAULT_GRAPH_COLOR);
        paint.setColor(graphColor);
        paint.setStrokeWidth(lineWidht);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        realWith = getWidth() - getPaddingLeft() - getPaddingRight();
        realHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        cellHeight = realHeight/yDensity;
        cellWith = realWith/xDensity;
        zeroByZero.set(getPaddingLeft(),getPaddingTop()+realHeight/2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLines(lines,paint);

    }

    public void setPoints(List<Integer> points) {
        if(points.size()<2){
            throw new IllegalArgumentException("Required at least two points");
        }else if(points.size()>xDensity){
            int deletable = points.size() - yDensity;
            for(int i = 0 ;i <deletable;i++){
                points.remove(i);
            }
        }
        this.points = points;
        invalidate();
    }
    public void setPoint(Integer point){
        if(points.size() == xDensity){
            points.remove(0);
        }else if(points.size() == 0){
            points.add(0);
        }
        points.add(point);
        populateLinePoints();
        invalidate();
    }
    private void populateLinePoints(){

        for(int i = 0;i<points.size();i++){
            if(i%2==1){
                float y =zeroByZero.y -  points.get(i/2)*cellHeight;
                lines[i] =y ; //y값
            }else {
                float x = zeroByZero.x+(i/2)*cellWith;
                lines[i] = x; //x값
            }
        }
    }
}
