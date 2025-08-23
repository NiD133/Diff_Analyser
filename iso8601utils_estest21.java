package com.google.gson.internal.bind.util;

import org.junit.Test;
import java.text.ParseException;
import java.text.ParsePosition;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link ISO8601Utils} class.
 */
public class ISO8601UtilsTest {

    /**
     * Verifies that the parse method throws a ParseException when given a date string
     * with a malformed year that has too many digits.
     */
    @Test
    public void parse_withMalformedDateString_throwsParseException() {
        // Arrange: Create a date string with a 9-digit year, which is an invalid format.
        String malformedDateString = "208737754-05-17T20:36:25Z";
        ParsePosition position = new ParsePosition(0); // Start parsing from the beginning.

        // Act & Assert
        try {
            ISO8601Utils.parse(malformedDateString, position);
            fail("Expected a ParseException for a malformed date string, but none was thrown.");
        } catch (ParseException e) {
            // SUCCESS: The expected exception was thrown.
            // For robustness, we can also verify the exception message.
            String expectedMessagePrefix = "Failed to parse date";
            assertTrue(
                "The exception message should indicate a parsing failure. Actual message: \"" + e.getMessage() + "\"",
                e.getMessage().startsWith(expectedMessagePrefix)
            );
        }
    }
}