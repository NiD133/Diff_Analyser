package com.google.gson.internal.bind.util;

import org.junit.Test;

import java.text.ParseException;
import java.text.ParsePosition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link ISO8601Utils}, focusing on parsing failure scenarios.
 */
public class ISO8601UtilsTest {

    /**
     * Verifies that parsing a date string with an excessively long year throws a ParseException.
     *
     * <p>The ISO 8601 standard expects a 4-digit year. This test provides a 9-digit year,
     * which causes the parser to fail when it encounters an unexpected character after
     * consuming what it believes to be a complete date-only value.
     */
    @Test
    public void parse_withInvalidYearFormat_shouldThrowParseException() {
        // Arrange: Define an invalid ISO 8601 date string and a starting position.
        // The year '190690348' is invalid as it exceeds the expected 4-digit length.
        String invalidDateString = "190690348-12-04T21:42:26Z";
        ParsePosition position = new ParsePosition(0);

        // Act & Assert: Attempt to parse the string and verify the correct exception is thrown.
        try {
            ISO8601Utils.parse(invalidDateString, position);
            fail("A ParseException was expected but not thrown.");
        } catch (ParseException e) {
            // The parser fails at index 8 (the character '8') because it expects a timezone indicator.
            String expectedMessage = "Failed to parse date [\"" + invalidDateString + "\"]: Invalid time zone indicator '8'";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}