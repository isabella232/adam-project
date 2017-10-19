package org.project.adam.util;
/**
 * Adam project
 * Copyright (C) 2017 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.res.StringRes;
import org.joda.time.ReadablePartial;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.project.adam.R;

@EBean
public class DateFormatter {

    @StringRes(R.string.long_day_format)
    protected String longDayFormat;

    @StringRes(R.string.short_day_format)
    protected String shortDayFormat;

    @StringRes(R.string.hour_of_day_format)
    protected String hourOfDayFormat;

    private DateTimeFormatter longDayFormatter;

    private DateTimeFormatter shortDayFormatter;

    private DateTimeFormatter hourOfDayFormatter;

    @AfterInject
    protected void buildFormatters(){
        longDayFormatter = DateTimeFormat.forPattern(longDayFormat);
        shortDayFormatter = DateTimeFormat.forPattern(shortDayFormat);
        hourOfDayFormatter = DateTimeFormat.forPattern(hourOfDayFormat);
    }


    public String shortDayFormat(ReadablePartial date) {
        return shortDayFormatter.print(date);
    }

    public String longDayFormat(ReadablePartial date) {
        return longDayFormatter.print(date);
    }

    public String hourOfDayFormat(ReadablePartial date) {
        return hourOfDayFormatter.print(date);
    }
}
