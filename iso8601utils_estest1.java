package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import org.junit.Test;

/**
 * Unit tests for {@link ISO8601Utils}.
 */
public class ISO8601UtilsTest {

    /**
     * Tests that parsing a valid ISO 8601 date-time string with milliseconds
     * and the 'Z' UTC designator produces the correct Date object.
     * The specific timestamp used is one millisecond before the Unix epoch.
     */
    @Test
    public void parse_validIso8601StringWithMilliseconds_returnsCorrectDate() throws ParseException {
        // Arrange
        String dateString = "1969-12-31T23:59:59.999Z";
        ParsePosition parsePosition = new ParsePosition(0);

        // The input string represents one millisecond before the Unix epoch (1970-01-01T00:00:00.000Z).
        // The epoch time in milliseconds is therefore -1.
        long expectedTimeMillis = -1L;

        // Act
        Date actualDate = ISO8601Utils.parse(dateString, parsePosition);

        // Assert
        // We assert against the underlying timestamp for accuracy and reliability,
        // avoiding the brittle and locale-dependent `Date.toString()` method.
        assertEquals(expectedTimeMillis, actualDate.getTime());

        // It's also good practice to verify that the ParsePosition was updated correctly,
        // indicating the entire string was consumed by the parser.
        assertEquals("ParsePosition should be at the end of the string",
            dateString.length(), parsePosition.getIndex());
    }
}