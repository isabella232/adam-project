package org.project.adam.util;

import org.androidannotations.annotations.EBean;
import org.joda.time.ReadablePartial;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@EBean
public class DateFormatter {

    private static final DateTimeFormatter DISPLAY_DATE_FORMAT = DateTimeFormat.forPattern("EEE dd MMM");

    private static final DateTimeFormatter LONG_DATE_FORMATTER = DateTimeFormat.forPattern("EEEE dd MMMM");

    private static final DateTimeFormatter HOUR_FORMATTER = DateTimeFormat.forPattern("HH:mm");

    public String shortFormatDay(ReadablePartial date) {
        return DISPLAY_DATE_FORMAT.print(date);
    }

    public String longFormatDay(ReadablePartial date) {
        return LONG_DATE_FORMATTER.print(date);
    }

    public String formatMinutesOfDay(ReadablePartial date) {
        return HOUR_FORMATTER.print(date);
    }
}
