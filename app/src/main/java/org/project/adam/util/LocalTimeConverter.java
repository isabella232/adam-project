package org.project.adam.util;

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
