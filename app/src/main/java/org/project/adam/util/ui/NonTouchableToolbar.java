package org.project.adam.util.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NonTouchableToolbar extends Toolbar{
    public NonTouchableToolbar(Context context) {
        super(context);
    }

    public NonTouchableToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NonTouchableToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
