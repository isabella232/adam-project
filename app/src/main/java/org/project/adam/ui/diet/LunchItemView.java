package org.project.adam.ui.diet;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.project.adam.R;
import org.project.adam.persistence.Lunch;
import org.project.adam.util.DateFormatters;

@EViewGroup(R.layout.lunch_item)
public class LunchItemView extends RelativeLayout {

    @ViewById(R.id.hour)
    TextView hourOfDay;
    @ViewById(R.id.content)
    TextView content;

    public LunchItemView(Context context) {
        super(context);
    }

    public void bind(final Lunch lunch) {
        this.content.setText(lunch.getContent());
        this.hourOfDay.setText(DateFormatters.formatMinutesOfDay(lunch.getTimeOfDay()));
    }


}