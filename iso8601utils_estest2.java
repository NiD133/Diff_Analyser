package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import org.junit.Test;

/**
 * Tests for {@link ISO8601Utils}.
 */
public class ISO8601UtilsTest {

    @Test
    public void format_withFarFutureDate_returnsCorrectISO8601String() {
        // Arrange
        // The target date is 208,737,754-05-17T20:36:25Z.
        // This specific date was derived from an auto-generated test case that used
        // large negative values in the Date constructor, causing calendar rollovers.
        // We construct the date explicitly here for better test clarity and maintainability.
        TimeZone utc = TimeZone.getTimeZone("UTC");
        Calendar calendar = new GregorianCalendar(utc);
        calendar.set(208737754, Calendar.MAY, 17, 20, 36, 25);
        calendar.set(Calendar.MILLISECOND, 0);
        Date farFutureDate = calendar.getTime();

        String expectedDateString = "208737754-05-17T20:36:25Z";

        // Act
        String actualDateString = ISO8601Utils.format(farFutureDate);

        // Assert
        assertEquals(expectedDateString, actualDateString);
    }
}