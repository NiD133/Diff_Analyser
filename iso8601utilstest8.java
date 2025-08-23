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

public class ISO8601UtilsTestTest8 {

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
    @SuppressWarnings("UndefinedEquals")
    public void testDateParseSpecialTimezone() throws ParseException {
        String dateStr = "2018-06-25T00:02:00-02:58";
        Date date = ISO8601Utils.parse(dateStr, new ParsePosition(0));
        GregorianCalendar calendar = createUtcCalendar();
        calendar.set(2018, Calendar.JUNE, 25, 3, 0);
        Date expectedDate = calendar.getTime();
        assertThat(date).isEqualTo(expectedDate);
    }
}
