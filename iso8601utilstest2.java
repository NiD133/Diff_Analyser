package com.google.gson.internal.bind.util;

import static com.google.common.truth.Truth.assertThat;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import org.junit.Test;

/**
 * Tests for {@link ISO8601Utils}.
 */
public class ISO8601UtilsTest {

    /**
     * Tests that {@link ISO8601Utils#format(Date, boolean)} correctly formats a date
     * with millisecond precision into a UTC-based ISO 8601 string.
     */
    @Test
    @SuppressWarnings("JavaUtilDate")
    public void format_withMilliseconds_returnsCorrectISO8601String() {
        // Arrange: Create a specific date: 2018-06-28T18:06:16.870Z
        TimeZone utc = TimeZone.getTimeZone("UTC");
        Calendar calendar = new GregorianCalendar(utc);
        calendar.set(2018, Calendar.JUNE, 28, 18, 6, 16);
        calendar.set(Calendar.MILLISECOND, 870);
        Date dateToFormat = calendar.getTime();

        String expectedDateString = "2018-06-28T18:06:16.870Z";

        // Act
        String actualDateString = ISO8601Utils.format(dateToFormat, true);

        // Assert
        assertThat(actualDateString).isEqualTo(expectedDateString);
    }
}