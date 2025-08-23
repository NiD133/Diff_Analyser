package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.ParsePosition;
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
     * Tests that parsing a valid ISO 8601 date-time string with a colon-separated
     * UTC offset ("-00:00") produces the correct Date object.
     */
    @Test
    public void parse_withUtcOffset_returnsCorrectDate() throws ParseException {
        // Arrange
        String dateString = "2014-02-14T20:21:21-00:00";
        ParsePosition parsePosition = new ParsePosition(0);

        // Create an expected Date object in UTC for a stable comparison
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        calendar.set(2014, Calendar.FEBRUARY, 14, 20, 21, 21);
        calendar.set(Calendar.MILLISECOND, 0);
        Date expectedDate = calendar.getTime();

        // Act
        Date actualDate = ISO8601Utils.parse(dateString, parsePosition);

        // Assert
        assertEquals(expectedDate, actualDate);
    }
}