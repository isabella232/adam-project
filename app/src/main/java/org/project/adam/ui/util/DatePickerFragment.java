package org.project.adam.ui.util;
/**
 * Adam project
 * Copyright (C) 2017 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import org.joda.time.LocalDate;

import lombok.Setter;
import lombok.experimental.Accessors;

@SuppressLint("Registered")
@Setter
@Accessors(chain = true)
public class DatePickerFragment extends DialogFragment {

    private DatePickerDialog.OnDateSetListener listener;

    private LocalDate initDate;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(initDate == null) {
            initDate = LocalDate.now();
        }
        return new DatePickerDialog(getActivity(), listener,
            initDate.getYear(),
            initDate.getMonthOfYear()-1,
            initDate.getDayOfMonth());
    }
}
