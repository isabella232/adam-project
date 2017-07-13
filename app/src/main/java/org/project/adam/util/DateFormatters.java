package org.project.adam.util;

import org.joda.time.ReadablePartial;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateFormatters {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("EEEE dd MMMM");

    private static final DateTimeFormatter HOUR_FORMATTER = DateTimeFormat.forPattern("HH:mm");


    public static final String formatDay(ReadablePartial date) {
        return DATE_FORMATTER.print(date);
    }

    public static final String formatMinutesOfDay(ReadablePartial date) {
        return HOUR_FORMATTER.print(date);
    }
}
