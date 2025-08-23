package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertEquals;

import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import org.junit.Test;

/**
 * Test for {@link ISO8601Utils}.
 * This class focuses on a specific test case for the parse method.
 */
public class ISO8601UtilsTest {

    @Test
    public void parse_withMillisecondsAndZuluTimeZone_shouldParseCorrectly() throws Exception {
        // Arrange
        String dateString = "2014-02-14T20:21:21.320Z";
        ParsePosition position = new ParsePosition(0);

        // Create an expected Date object for a robust comparison. This avoids the
        // brittleness of comparing string formats, which can be locale-dependent.
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        calendar.set(2014, Calendar.FEBRUARY, 14, 20, 21, 21);
        calendar.set(Calendar.MILLISECOND, 320);
        Date expectedDate = calendar.getTime();

        // Act
        Date actualDate = ISO8601Utils.parse(dateString, position);

        // Assert
        assertEquals(expectedDate, actualDate);
        // Also, verify that the parser consumed the entire string.
        assertEquals("The parse position should be at the end of the string.",
            dateString.length(), position.getIndex());
    }
}