package org.project.adam.ui.dashboard.glycaemia;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import org.androidannotations.annotations.EViewGroup;
import org.project.adam.R;

/**
 * Created by bastien on 22/06/2017.
 */
@EViewGroup(R.layout.view_glycaemia_add)
public class GlycaemiaAddView extends RelativeLayout {
    public GlycaemiaAddView(Context context) {
        super(context);
    }

    public GlycaemiaAddView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
