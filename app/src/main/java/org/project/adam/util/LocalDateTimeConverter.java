package org.project.adam.util;

import android.arch.persistence.room.TypeConverter;

import org.joda.time.LocalDateTime;

public class LocalDateTimeConverter {
    @TypeConverter
    public LocalDateTime fromTimestamp(Long value) {
        return value == null ? null : new LocalDateTime(value);
    } 
 
    @TypeConverter 
    public Long toTimestamp(LocalDateTime date) {
        return date == null ? null : date.toDate().getTime();
    }
} 