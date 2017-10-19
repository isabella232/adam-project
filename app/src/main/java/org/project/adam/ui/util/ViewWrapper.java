package org.project.adam.ui.util;
/**
 * Adam project
 * Copyright (C) 2017 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;

import lombok.Getter;

public class ViewWrapper<V extends View> extends RecyclerView.ViewHolder {

    @Getter
    private V view;

    public ViewWrapper(V itemView) {
        super(itemView);
        view = itemView;
    }
}