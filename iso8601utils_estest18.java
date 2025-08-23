package com.google.gson.internal.bind.util;

import org.junit.Test;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link ISO8601Utils}.
 * This refactored version focuses on clarity and robustness.
 */
public class ISO8601UtilsTest {

    /**
     * Verifies that parsing a valid ISO 8601 date-time string with the UTC 'Z'
     * designator produces the correct Date object.
     */
    @Test
    public void parse_whenGivenFullDateTimeWithUtcTimeZone_shouldReturnCorrectDate() throws ParseException {
        // Arrange: Define the ISO 8601 string to be parsed.
        String iso8601DateTimeString = "2014-02-14T20:21:21Z";
        ParsePosition parsePosition = new ParsePosition(0);

        // Arrange: Create the expected Date object for a reliable comparison.
        // Using a Calendar instance set to UTC ensures the test is not
        // dependent on the system's default time zone, making it robust.
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(2014, Calendar.FEBRUARY, 14, 20, 21, 21);
        calendar.set(Calendar.MILLISECOND, 0);
        Date expectedDate = calendar.getTime();

        // Act: Parse the string using the utility method under test.
        Date actualDate = ISO8601Utils.parse(iso8601DateTimeString, parsePosition);

        // Assert: Verify that the parsed date matches the expected date.
        // This is more reliable than comparing string representations.
        assertEquals(expectedDate, actualDate);
    }
}