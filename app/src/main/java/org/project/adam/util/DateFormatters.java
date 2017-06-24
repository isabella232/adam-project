package org.project.adam.util;

import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormatters {

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("EEEE dd MMMM");
    private static final SimpleDateFormat HOUR_FORMATTER = new SimpleDateFormat("HH:mm");

    @NonNull
    public static Calendar getCalendarFromMinutesOfDay(int minutesOfDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.add(Calendar.MINUTE, minutesOfDay);
        return calendar;
    }

    public static final String formatMinutesOfDay(int minutesOfDay) {
        Calendar calendar = getCalendarFromMinutesOfDay(minutesOfDay);
        return HOUR_FORMATTER.format(calendar.getTime());
    }

    public static final String formatDay(Date date) {
        return DATE_FORMATTER.format(date);
    }

    public static final String formatMinutesOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return HOUR_FORMATTER.format(calendar.getTime());
    }
}
