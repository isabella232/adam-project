package org.project.adam.ui.diet;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.joda.time.LocalTime;
import org.project.adam.R;
import org.project.adam.persistence.Meal;
import org.project.adam.util.DateFormatters;

@EViewGroup(R.layout.meal_item)
public class MealItemView extends RelativeLayout {

    @ViewById(R.id.hour)
    TextView hourOfDay;
    @ViewById(R.id.content)
    TextView content;

    public MealItemView(Context context) {
        super(context);
    }

    public void bind(final Meal meal) {
        this.content.setText(meal.getContent());
        this.hourOfDay.setText(DateFormatters
            .formatMinutesOfDay(meal.getTimeOfDay()));
    }


}