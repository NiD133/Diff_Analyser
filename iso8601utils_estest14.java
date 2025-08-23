package com.google.gson.internal.bind.util;

import org.junit.Test;

import java.text.ParseException;
import java.text.ParsePosition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ISO8601Utils_ESTestTest14 {

    /**
     * Tests that calling {@link ISO8601Utils#parse(String, ParsePosition)} with an empty string
     * correctly throws a {@link ParseException}.
     * <p>
     * An empty string is not a valid ISO 8601 date, so the parsing operation is expected to fail.
     */
    @Test
    public void parse_withEmptyString_shouldThrowParseException() {
        // Arrange: An empty string to parse and a ParsePosition starting at the beginning.
        String emptyDateString = "";
        ParsePosition position = new ParsePosition(0);

        // Act & Assert: Attempt to parse the empty string and verify the expected exception.
        try {
            ISO8601Utils.parse(emptyDateString, position);
            fail("A ParseException should have been thrown for an empty date string, but it was not.");
        } catch (ParseException e) {
            // Verify that the correct exception was thrown.
            // The implementation fails with a NumberFormatException when trying to parse the year
            // from an empty string, which is then wrapped in a ParseException.
            String expectedMessage = "Failed to parse date [\"\"]: For input string: \"\"";
            assertEquals(expectedMessage, e.getMessage());

            // Verify that the cause of the failure is indeed a NumberFormatException.
            assertNotNull("The ParseException should have a cause.", e.getCause());
            assertTrue("The cause should be a NumberFormatException.", e.getCause() instanceof NumberFormatException);
        }
    }
}