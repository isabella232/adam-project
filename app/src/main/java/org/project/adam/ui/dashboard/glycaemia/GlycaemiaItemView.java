package org.project.adam.ui.dashboard.glycaemia;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.joda.time.LocalTime;
import org.project.adam.Preferences;
import org.project.adam.Preferences_;
import org.project.adam.R;
import org.project.adam.persistence.Glycaemia;
import org.project.adam.util.DateFormatters;

/**
 * Created by bastien on 22/06/2017.
 */
@EViewGroup(R.layout.view_glycaemia_item)
public class GlycaemiaItemView extends RelativeLayout {

    private static final int DANGEROUS_GLYCAEMIA_THRESHOLD = 60;

    @Pref
    Preferences_ preferences;

    @ViewById(R.id.status_color)
    View statusColor;

    @ViewById(R.id.glycaemia_value_mg_Dl)
    TextView glycaemiaValue;

    @ViewById(R.id.glycaemia_date)
    TextView glycaemiaDate;

    @ViewById(R.id.glycaemia_comment)
    TextView glycaemiaComment;

    public GlycaemiaItemView(Context context) {
        super(context);
    }

    public GlycaemiaItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void bind(Glycaemia glycaemia) {
        glycaemiaValue.setText(getResources().getString(R.string.glycaemia_value_format, glycaemia.getValue()));
        glycaemiaDate.setText(DateFormatters.formatMinutesOfDay(glycaemia.getDate()));
        glycaemiaComment.setText(glycaemia.getComment());
        int color = glycaemia.getValue() < preferences.riskGly().get() ?
            getResources().getColor(R.color.sunflower_yellow) :
            getResources().getColor(R.color.glycaemia_green);
        statusColor.setBackgroundColor(color);
    }
}
