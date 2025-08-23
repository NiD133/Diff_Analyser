package com.google.gson.internal.bind.util;

import org.junit.Test;
import java.text.ParseException;
import java.text.ParsePosition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link ISO8601Utils} class.
 */
public class ISO8601UtilsTest {

    /**
     * Verifies that calling parse() with a null input string results in a ParseException.
     * The internal parsing logic is expected to fail with a NumberFormatException when
     * it attempts to process the null string, which is then wrapped in a ParseException.
     */
    @Test
    public void parse_withNullInputString_shouldThrowParseException() {
        // Arrange
        String nullDateString = null;
        ParsePosition position = new ParsePosition(0);

        // Act & Assert
        try {
            ISO8601Utils.parse(nullDateString, position);
            fail("A ParseException was expected for null input, but was not thrown.");
        } catch (ParseException e) {
            // Verify the exception message clearly indicates the failure.
            String expectedMessagePrefix = "Failed to parse date [null]";
            assertTrue(
                "Exception message should start with: '" + expectedMessagePrefix + "'",
                e.getMessage().startsWith(expectedMessagePrefix)
            );

            // Verify the underlying cause of the parsing failure.
            Throwable cause = e.getCause();
            assertNotNull("The ParseException should have an underlying cause.", cause);
            assertEquals(
                "The cause of the failure should be a NumberFormatException.",
                NumberFormatException.class,
                cause.getClass()
            );
        }
    }
}