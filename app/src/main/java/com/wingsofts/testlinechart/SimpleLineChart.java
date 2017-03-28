package com.wingsofts.testlinechart;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

public class SimpleLineChart extends View {
    private float YoffsetBottom = 60;// 单位：px  控件偏移底部 的距离
    private float YoffsetTop = 90;// 单位：px  控件偏移顶部 的距离
    private float XoffsetLeft = 30;// 单位：px  控件偏移底部 的距离
    private float XoffsetRight = 30;// 单位：px  控件偏移底部 的距离
    private int mWidth, mHeight;
    private float mYAxisFontSize = 24;
    private int mLineColor = Color.parseColor("#00BCD4");
    private float mStrokeWidth = 8.0f;
    private HashMap<String, Integer> mPointMap1;
    private HashMap<String, Integer> mPointMap2;
    private float mPointRadius = 16;
    private String[] mXAxis = {};
    private String[] mYAxis = {};
    private Paint xyTextPaint = getXYTextPaint();
    private Paint xyPaint = getXYPaint();
    private Paint dotPaint = getDotPaint();
    private Paint linePaint = getLinePaint();
    private int[] xPoints;

    public SimpleLineChart(Context context) {
        this(context, null);
    }

    public SimpleLineChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleLineChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            throw new IllegalArgumentException("width must be EXACTLY,you should set like android:width=\"200dp\"");
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else if (widthMeasureSpec == MeasureSpec.AT_MOST) {

            throw new IllegalArgumentException("height must be EXACTLY,you should set like android:height=\"200dp\"");
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (mXAxis.length == 0 || mYAxis.length == 0) {
            throw new IllegalArgumentException("X or Y items is null");
        }
        drawY(canvas);
        drawX(canvas);
        if (mPointMap1 != null && mPointMap1.size() > 0) {
            int yInterval = drawYKedu(canvas);
            drawYKedu(canvas, yInterval);
            paintLineInfo(canvas, mPointMap1, true);
            if (mPointMap2 != null && mPointMap2.size() > 0)
                paintLineInfo(canvas, mPointMap2, false);
        }
    }

    private void drawYKedu(Canvas canvas, int yInterval) {
        //x轴的刻度集合
        xPoints = new int[mXAxis.length];
        //计算x轴 刻度间距
        int xInterval = (int) ((mWidth - XoffsetLeft - XoffsetRight) / (mXAxis.length));
        //获取X轴刻度Y坐标
        int xItemY = (int) (mYAxisFontSize + mYAxis.length * yInterval);

        for (int i = 0; i < mXAxis.length; i++) {
            float xValue1 = (i + 1) * xInterval;
            //X轴的刻度
            canvas.drawLine(xValue1, xItemY + YoffsetTop - 10, xValue1, xItemY + YoffsetTop + 6, getXYPaint());
            //x轴文本
            canvas.drawText(mXAxis[i], xValue1, xItemY + YoffsetTop + 40, xyTextPaint);
            xPoints[i] = (int) xValue1;
        }
    }

    private int drawYKedu(Canvas canvas) {
        //计算Y轴 每个刻度的间距
        int yInterval = (int) ((mHeight - YoffsetBottom - YoffsetTop - mYAxisFontSize - 2) / (mYAxis.length));
        for (int i = 0; i < mYAxis.length; i++) {
            float yValue = getHeight() - YoffsetBottom - ((1 + i) * yInterval);
            //y轴的刻度
            canvas.drawLine(0 + XoffsetLeft, yValue, 10 + XoffsetLeft, yValue, getXYPaint());
        }
        return yInterval;
    }

    /**
     * 折线信息
     *
     * @param canvas
     */
    private void paintLineInfo(Canvas canvas, HashMap<String, Integer> mPointMap, boolean needText) {


        //拿到最大值
        int maxValue = getMaxValue();
        float v = (getHeight() - YoffsetTop - YoffsetBottom) / 5; //y轴一格对应的px
        int i1 = maxValue / 5;  //y轴一格对应的值

        int[] mYPoints = new int[mYAxis.length];
        for (int i = 0; i < mXAxis.length; i++) {
            float yPx = (getHeight() - YoffsetBottom + YoffsetTop) - (mPointMap.get(mXAxis[i]) / i1 * v);
            mYPoints[i] = (int) yPx;
        }

        //画点
        if (needText) {
            dotPaint.setColor(Color.parseColor("#2394ff"));
            linePaint.setColor(Color.parseColor("#2394ff"));
        } else {
            dotPaint.setColor(Color.parseColor("#79d21f"));
            linePaint.setColor(Color.parseColor("#79d21f"));
        }
        for (int i = 0; i < mXAxis.length; i++) {
            if (mPointMap.get(mXAxis[i]) == null) {
                throw new IllegalArgumentException("PointMap has incomplete data!");
            }
            //连线
            if (i > 0)
                canvas.drawLine(xPoints[i - 1], mYPoints[i - 1 >= 0 ? i - 1 : 0], xPoints[i], mYPoints[i], linePaint);
        }

        for (int i = 0; i < mXAxis.length; i++) {
            if (mPointMap.get(mXAxis[i]) == null) {
                throw new IllegalArgumentException("PointMap has incomplete data!");
            }
            //画点
            Paint dotPaint2 = new Paint();
            dotPaint2.setColor(Color.parseColor("#ffffff"));
            dotPaint2.setStyle(Paint.Style.FILL); //空心圆
            dotPaint2.setStrokeWidth(mPointRadius / 2);
            canvas.drawCircle(xPoints[i], mYPoints[i], mPointRadius, dotPaint2);
            canvas.drawCircle(xPoints[i], mYPoints[i], mPointRadius, dotPaint);
        }

        //显示标题的图片
        if (needText) {
            for (int i = 0; i < mXAxis.length; i++) {
                //点上面画文字
                TextView tv = new TextView(getContext());
                tv.setBackgroundResource(R.mipmap.bg_chart_text);
                String mXAxi = mXAxis[i];
                tv.setText(String.valueOf(mPointMap.get(mXAxi)));
                tv.setGravity(Gravity.CENTER);
                Drawable drawable = convertViewToDrawable(tv);
                Bitmap bitmap = drawableToBitamp(drawable);
                canvas.drawBitmap(bitmap, xPoints[i] - bitmap.getWidth() / 2, mYPoints[i] - bitmap.getHeight()-20, null);
            }
        }
    }

    private int getMaxValue() {

        int max = mPointMap1.get(mXAxis[0]);

        for (int i = 1; i < mXAxis.length; i++) {
            if (mPointMap1.get(mXAxis[i]) > max) {
                max = mPointMap1.get(mXAxis[i]);
            }
        }
        return max;
    }

    private Drawable convertViewToDrawable(View view) {
        int spec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        view.measure(spec, spec);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
//        view.layout(0, 0, 60, 44);
        Bitmap b = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        c.translate(-view.getScrollX(), -view.getScrollY());
        view.draw(c);
        view.setDrawingCacheEnabled(true);
        Bitmap cacheBmp = view.getDrawingCache();
        Bitmap viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
        view.destroyDrawingCache();
        return new BitmapDrawable(getResources(), viewBmp);
    }

    private Bitmap drawableToBitamp(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        System.out.println("Drawable转Bitmap");
        Bitmap.Config config =
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);

        return bitmap;
    }


    private Paint getDotPaint() {
        Paint dotPaint = new Paint();
        dotPaint.setColor(mLineColor);
        dotPaint.setStyle(Paint.Style.STROKE); //空心圆
        dotPaint.setStrokeWidth(mPointRadius/2);
        return dotPaint;
    }

    private Paint getLinePaint() {
        Paint linePaint = new Paint();
        linePaint.setColor(mLineColor);
        linePaint.setAntiAlias(true);
        //设置线条宽度
        linePaint.setStrokeWidth(mStrokeWidth);
        return linePaint;
    }

    private Paint getXYPaint() {
        Paint yPaint = new Paint();
        yPaint.setColor(Color.parseColor("#cccccc"));
        yPaint.setStrokeWidth((float) 2.0);
        return yPaint;
    }

    private Paint getXYTextPaint() {
        //画坐标线的轴
        Paint axisPaint = new Paint();
        axisPaint.setTextSize(mYAxisFontSize);
        axisPaint.setColor(Color.parseColor("#999999"));
        return axisPaint;
    }

    /**
     * 画xy轴
     *
     * @param canvas
     */
    private void drawX(Canvas canvas) {
          /*画x的坐标轴*/
        canvas.drawLine(0 + XoffsetLeft, getHeight() - YoffsetBottom, getWidth(), getHeight() - YoffsetBottom, xyPaint);      //绘制直线
        Drawable drawable = getResources().getDrawable(R.drawable.arrow_chart_right);
        Bitmap bitmap = drawableToBitamp(drawable);
        canvas.drawBitmap(bitmap,getWidth()-bitmap.getWidth(),getHeight() - YoffsetBottom-bitmap.getHeight()/2,null);
    }

    /**
     * 画xy轴
     *
     * @param canvas
     */
    private void drawY(Canvas canvas) {
            /*画y的坐标轴*/
        canvas.drawLine(0 + XoffsetLeft, getHeight() - YoffsetBottom, 0 + XoffsetLeft, 0, xyPaint);      //绘制直线
        Drawable drawable = getResources().getDrawable(R.drawable.arrow_chart_up);
        Bitmap bitmap = drawableToBitamp(drawable);
        canvas.drawBitmap(bitmap,0 + XoffsetLeft-bitmap.getWidth()/2, 0,null);
    }

    /**
     * 设置map数据
     *
     * @param data1
     */
    public void setLine1Data(HashMap<String, Integer> data1) {
        mPointMap1 = data1;
        invalidate();
    }

    public void setLine2Data(HashMap<String, Integer> data2) {
        mPointMap2 = data2;
        invalidate();
    }
    public void setYItem(String[] yItem) {
        mYAxis = yItem;
    }
    public void setXItem(String[] xItem) {
        mXAxis = xItem;
    }

}
