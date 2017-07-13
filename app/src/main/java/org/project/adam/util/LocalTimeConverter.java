package org.project.adam.util;

import android.arch.persistence.room.TypeConverter;

import org.joda.time.LocalTime;

public class LocalTimeConverter {

    @TypeConverter
    public LocalTime fromMinutes(Integer value){
        return value == null ? null : LocalTime.fromMillisOfDay(value*1000*60);
    }

    @TypeConverter
    public Integer toMinutes(LocalTime time){
        return time == null ? null : time.getMillisOfDay() / (60 * 1000);
    }
}
