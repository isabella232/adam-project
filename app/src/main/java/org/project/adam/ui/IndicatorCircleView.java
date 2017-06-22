package org.project.adam.ui;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EView;
import org.project.adam.persistence.Lunch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Setter;
import timber.log.Timber;


@EView
public class IndicatorCircleView extends View implements ViewPager.OnPageChangeListener {
    
    private static final int COLOR_ARC = Color.WHITE;
    private static final int COLOR_ARC_PAST = Color.LTGRAY;
    private static final int COLOR_POINTS = Color.BLUE;

    protected ViewPager viewPager;
    private Paint shadowPaint;
    private Paint arcPaint;
    private GestureDetector gestureDetector;
    private float ww;
    private float hh;


    @Setter
    private int selectedItem = -1;

    private RectF bounds = new RectF(0, 0, 0, 0);
    /**
     * from 0 to 100
     */
    private List<Float> indicators = new ArrayList<>();

    private Map<RectF,Integer> actionMap = new HashMap<>();

    public IndicatorCircleView(Context context) {
        super(context);
    }

    public IndicatorCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public IndicatorCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setViewPager(ViewPager viewPager) {
        viewPager.addOnPageChangeListener(this);
        this.viewPager = viewPager;
    }

    class mListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            handleTouchOn(e.getX(),e.getY());
            return super.onSingleTapUp(e);
        }
    }

    private void handleTouchOn(float x, float y) {
        for (Map.Entry<RectF, Integer> entry : actionMap.entrySet()) {
            Timber.d(entry.getKey() + "/" + entry.getValue()+ "on "+x+ " and "+y);
            if(entry.getKey().contains(x,y)){Timber.d("Yes");
                viewPager.setCurrentItem(entry.getValue());
                return;
            }

        }
    }

    @AfterInject
    public void init() {
        arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeWidth(10);

        shadowPaint = new Paint(0);
        shadowPaint.setColor(0xff101010);
        shadowPaint.setStyle(Paint.Style.STROKE);
        shadowPaint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));

        gestureDetector = new GestureDetector(getContext(), new mListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    /**
     * TODO move somewhere else
     * must be ordered, otherwise order later
     */
    public void setMeals(List<Lunch> meals) {
        int minTime = Integer.MAX_VALUE;
        int maxTime = 0;

        for (Lunch lunch : meals) {
            int time = lunch.getTimeOfDay();
            if (time > maxTime) {
                maxTime = time;
            }
            if (time < minTime) {
                minTime = time;
            }
        }

        for (Lunch lunch : meals) {
            float progress = (float) (lunch.getTimeOfDay() - minTime) / (float) (maxTime - minTime);
            Timber.d("computed progress %f", progress);
            indicators.add(progress);
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // Account for padding
        float xpad = (float) (getPaddingLeft() + getPaddingRight());
        float ypad = (float) (getPaddingTop() + getPaddingBottom());

        // Account for the label
        //compute detla if (mShowText) xpad += mTextWidth;

        ww = (float) w - xpad * 2;
        hh = (float) h - ypad * 2;


        bounds.set(xpad, ypad, xpad + ww, ypad + hh * 2);
    }


    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the shadow
      /*  canvas.drawOval(
            mShadowBounds,
            shadowPaint
        );*/

        // Draw the label text

        if (canvas != null) {
            // Draw the pie slices


            //draw full line
            arcPaint.setColor(COLOR_ARC);
            canvas.drawArc(bounds, 180, 180, false, shadowPaint);
            canvas.drawArc(bounds, 180, 180, false, arcPaint);


            int i = 0;
            //first search for selected item, cause need to be draw first
            for (Float item : indicators) {
                if (i == selectedItem) {
                    arcPaint.setColor(COLOR_ARC_PAST);
                    canvas.drawArc(bounds, 180, item * 180, false, arcPaint);
                    break;
                }
                i++;
            }

            arcPaint.setColor(COLOR_POINTS);
            i = 0;
            for (Float item : indicators) {
                if (i == selectedItem) {
                    arcPaint.setStyle(Paint.Style.FILL);
                } else {
                    arcPaint.setStyle(Paint.Style.STROKE);
                }
                double x1 = Math.cos(Math.PI * item);
                double x2 = bounds.centerX();


                float circleRadius = ww / 50;
                float x = (float) (bounds.centerX() - ww / 2 * Math.cos(Math.PI * item));
                float y = (float) (bounds.centerY() - hh * Math.sin(Math.PI * item));
                canvas.drawCircle(x, y, circleRadius, arcPaint);
                actionMap.put(new RectF(x-circleRadius,y-circleRadius,x+circleRadius,y+circleRadius),i);
                i++;
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        selectedItem = position;
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


}
