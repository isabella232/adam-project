package org.project.adam.ui.dashboard.meal;
/**
 * Adam project
 * Copyright (C) 2017 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.project.adam.BaseFragment;
import org.project.adam.R;
import org.project.adam.persistence.Meal;

@EFragment(R.layout.fragment_meal)
public class MealDetailFragment extends BaseFragment {

    @ViewById(R.id.content)
    TextView content;

    private Meal meal;

    public void bind(Meal meal) {
        this.meal = meal;
    }

    @AfterViews
    void init() {
        if(meal!=null){
            content.setText(meal.getContent());
        }
    }
}
