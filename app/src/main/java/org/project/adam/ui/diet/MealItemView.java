package org.project.adam.ui.diet;
/**
 * Adam project
 * Copyright (C) 2017 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.project.adam.R;
import org.project.adam.persistence.Meal;
import org.project.adam.util.DateFormatter;

@EViewGroup(R.layout.meal_item)
public class MealItemView extends RelativeLayout {

    @ViewById(R.id.hour)
    protected TextView hourOfDay;

    @ViewById(R.id.content)
    protected TextView content;

    @Bean
    protected DateFormatter dateFormatter;

    public MealItemView(Context context) {
        super(context);
    }

    public void bind(final Meal meal) {
        this.content.setText(meal.getContent());
        this.hourOfDay.setText(dateFormatter.hourOfDayFormat(meal.getTimeOfDay()));
    }


}