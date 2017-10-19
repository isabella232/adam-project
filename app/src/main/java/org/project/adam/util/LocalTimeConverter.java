package org.project.adam.util;
/**
 * Adam project
 * Copyright (C) 2017 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import android.arch.persistence.room.TypeConverter;

import org.joda.time.LocalTime;

public class LocalTimeConverter {

    @TypeConverter
    public LocalTime fromMinutes(Integer value){
        return value == null ? null : new LocalTime(value / 60, value % 60);
    }

    @TypeConverter
    public Integer toMinutes(LocalTime time){
        return time == null ? null : time.getHourOfDay() * 60 + time.getMinuteOfHour();
    }
}
