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
import org.project.adam.persistence.Diet;

@EViewGroup(R.layout.diet_item)
public class DietItemView extends RelativeLayout {

    @ViewById(R.id.name)
    TextView name;
    @ViewById(R.id.status)
    TextView status;

    @Bean
    DietUtils dietUtils;

    public DietItemView(Context context) {
        super(context);
    }

    public void bind(final Diet diet) {
        final boolean isCurrent = dietUtils.isCurrent(diet);

        this.name.setText(diet.getName());
        this.name.setTextColor(getResources().getColor(isCurrent ? R.color.black : R.color.grey));
        this.status.setVisibility(isCurrent ? VISIBLE : GONE);
    }


}