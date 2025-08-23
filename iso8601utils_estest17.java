package com.google.gson.internal.bind.util;

import org.junit.Test;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link ISO8601Utils}.
 */
public class ISO8601UtilsTest {

    /**
     * Tests that parsing a complete ISO 8601 date-time string with milliseconds
     * and a UTC timezone offset ("+00:00") produces the correct Date object.
     */
    @Test
    public void parse_fullDateTimeWithMillisAndUtcOffset_returnsCorrectDate() throws ParseException {
        // Arrange
        String iso8601DateTimeString = "2014-02-14T20:21:22.575+00:00";
        ParsePosition parsePosition = new ParsePosition(0);

        // Create the expected Date object to ensure a robust, type-safe comparison.
        // This avoids relying on the brittle Date.toString() format.
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        calendar.set(2014, Calendar.FEBRUARY, 14, 20, 21, 22);
        calendar.set(Calendar.MILLISECOND, 575);
        Date expectedDate = calendar.getTime();

        // Act
        Date actualDate = ISO8601Utils.parse(iso8601DateTimeString, parsePosition);

        // Assert
        assertEquals("The parsed Date object should match the expected value.", expectedDate, actualDate);
        assertEquals("The parser should consume the entire string.", iso8601DateTimeString.length(), parsePosition.getIndex());
    }
}