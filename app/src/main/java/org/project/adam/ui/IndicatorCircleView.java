package org.project.adam.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
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
import org.joda.time.LocalTime;
import org.project.adam.R;
import org.project.adam.persistence.Meal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Setter;
import timber.log.Timber;


@EView
public class IndicatorCircleView extends View implements ViewPager.OnPageChangeListener {

    private static final int COLOR_ARC = Color.WHITE;
    private static final int COLOR_ARC_PAST = Color.DKGRAY;
    private static final int SMALL_CIRCUS_RADIUS_IN_SP = 5;
    private static final int BIG_CIRCUS_RADIUS_IN_SP = 8;
    private ViewPager viewPager;
    private Paint arcPaint;
    private Paint circlePaint;
    private GestureDetector gestureDetector;
    private float ww;
    private float hh;
    private int fillColor;
    private int bigCircleRadius;
    private int smallCircleRadius;

    /**
     * from 0 to 100
     */
    private List<Float> indicators = new ArrayList<>();

    private Map<RectF, Integer> actionMap = new HashMap<>();

    @Setter
    private int selectedItem = -1;

    @Setter
    private int nextMealPosition = -1;

    private RectF bounds = new RectF(0, 0, 0, 0);

    public IndicatorCircleView(Context context) {
        super(context);
    }

    public IndicatorCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
    }

    public IndicatorCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.IndicatorCircleView, 0, 0);
        try {
            fillColor = a.getColor(R.styleable.IndicatorCircleView_circleFillColor, Color.BLACK);
        } finally {
            a.recycle();
        }
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
            handleTouchOn(e.getX(), e.getY());
            return super.onSingleTapUp(e);
        }
    }

    private void handleTouchOn(float x, float y) {
        for (Map.Entry<RectF, Integer> entry : actionMap.entrySet()) {
            Timber.d(entry.getKey() + "/" + entry.getValue() + "on " + x + " and " + y);
            if (entry.getKey().contains(x, y)) {
                Timber.d("Yes");
                viewPager.setCurrentItem(entry.getValue());
                return;
            }
        }
    }

    @AfterInject
    public void init() {

        float scale = getContext().getResources().getDisplayMetrics().density;

        arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeWidth(7);
        arcPaint.setPathEffect(new DashPathEffect(new float[]{15, 7}, 0));

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(7);

        gestureDetector = new GestureDetector(getContext(), new mListener());

        smallCircleRadius = (int) (SMALL_CIRCUS_RADIUS_IN_SP * scale + 0.5f);
        bigCircleRadius = (int) (BIG_CIRCUS_RADIUS_IN_SP * scale + 0.5f);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    /**
     * TODO move somewhere else
     * must be ordered, otherwise order later
     */
    public void setMeals(List<Meal> meals) {
        indicators.clear();
        LocalTime minTime =  LocalTime.MIDNIGHT.minusMillis(1);
        LocalTime maxTime = LocalTime.MIDNIGHT;

        for (Meal meal : meals) {
            LocalTime time = meal.getTimeOfDay();
            if (time.isAfter(maxTime)) {
                maxTime = time;
            }
            if (time.isBefore(minTime)) {
                minTime = time;
            }
        }
        float maxAmplitude = maxTime.getMillisOfDay() - minTime.getMillisOfDay();
        for (Meal meal : meals) {
            float progress = ((float) (meal.getTimeOfDay().getMillisOfDay() - minTime.getMillisOfDay())) / maxAmplitude;
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


        if (canvas != null) {

            arcPaint.setColor(COLOR_ARC);

            int i = 0;
            //first search for selected item, cause need to be draw first
            for (Float item : indicators) {
                if (i == nextMealPosition) {
                    arcPaint.setColor(COLOR_ARC_PAST);
                    canvas.drawArc(bounds, 180, item * 180, false, arcPaint);
                    arcPaint.setColor(COLOR_ARC);
                    canvas.drawArc(bounds, 180 + item * 180, 180 - item * 180, false, arcPaint);
                    break;
                }
                i++;
            }

            i = 0;
            actionMap.clear();

            boolean overCurrentMeal = false;
            for (Float item : indicators) {

                float circleRadius = smallCircleRadius;

                //compute coordinates
                double x1 = Math.cos(Math.PI * item);
                double x2 = bounds.centerX();

                float x = (float) (bounds.centerX() - ww / 2 * Math.cos(Math.PI * item));
                float y = (float) (bounds.centerY() - hh * Math.sin(Math.PI * item));

                //first draw a circle to "erase the background"
                circlePaint.setColor(fillColor);
                circlePaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(x, y, circleRadius, circlePaint);

                //set default color
                circlePaint.setColor(COLOR_ARC_PAST);


                //handle special cases
                if (i == nextMealPosition) {
                    overCurrentMeal = true;
                }
                if (i == selectedItem) {
                    circlePaint.setStyle(Paint.Style.FILL);
                    circlePaint.setColor(COLOR_ARC);
                    circleRadius = bigCircleRadius;
                } else {
                    circlePaint.setStyle(Paint.Style.STROKE);
                    circlePaint.setColor(overCurrentMeal ? COLOR_ARC : COLOR_ARC_PAST);
                }

                //draw final circle and associate action zone
                canvas.drawCircle(x, y, circleRadius, circlePaint);
                actionMap.put(new RectF(x - circleRadius * 2, y - circleRadius * 2, x + circleRadius * 2, y + circleRadius * 2), i);

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
