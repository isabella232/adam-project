package org.project.adam.ui.dashboard.glycaemia;
/**
 * Adam project
 * Copyright (C) 2017 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import org.androidannotations.annotations.EViewGroup;
import org.project.adam.R;


@EViewGroup(R.layout.view_glycaemia_add)
public class GlycaemiaAddView extends RelativeLayout {
    public GlycaemiaAddView(Context context) {
        super(context);
    }

    public GlycaemiaAddView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
