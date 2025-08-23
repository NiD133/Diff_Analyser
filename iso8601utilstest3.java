package com.google.gson.internal.bind.util;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import org.junit.Test;

public class ISO8601UtilsTestTest3 {

    private static TimeZone utcTimeZone() {
        return TimeZone.getTimeZone("UTC");
    }

    private static GregorianCalendar createUtcCalendar() {
        TimeZone utc = utcTimeZone();
        GregorianCalendar calendar = new GregorianCalendar(utc);
        // Calendar was created with current time, must clear it
        calendar.clear();
        return calendar;
    }

    @Test
    @SuppressWarnings("JavaUtilDate")
    public void testDateFormatWithTimezone() {
        long time = 1530209176870L;
        Date date = new Date(time);
        String dateStr = ISO8601Utils.format(date, true, TimeZone.getTimeZone("Brazil/East"));
        String expectedDate = "2018-06-28T15:06:16.870-03:00";
        assertThat(dateStr).isEqualTo(expectedDate);
    }
}
