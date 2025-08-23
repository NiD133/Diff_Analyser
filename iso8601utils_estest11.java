package com.google.gson.internal.bind.util;

import org.junit.Test;
import java.text.ParseException;
import java.text.ParsePosition;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link ISO8601Utils}.
 * This focuses on improving the understandability of a single, auto-generated test case.
 */
public class ISO8601UtilsTest {

    /**
     * Verifies that {@link ISO8601Utils#parse(String, ParsePosition)} throws a
     * {@link ParseException} when given a string that does not conform to the
     * expected ISO 8601 format.
     */
    @Test
    public void parse_shouldThrowParseException_forInvalidDateString() {
        // Arrange: Define an input string that is clearly not a valid date.
        String invalidDateString = "&R[&";
        ParsePosition position = new ParsePosition(0);

        // Act & Assert: Attempt to parse the invalid string and verify the exception.
        try {
            ISO8601Utils.parse(invalidDateString, position);
            fail("A ParseException should have been thrown for the invalid date string.");
        } catch (ParseException e) {
            // Verify that the exception message clearly indicates the failure reason.
            String expectedMessage = "Failed to parse date [\"" + invalidDateString + "\"]: Invalid number: " + invalidDateString;
            assertEquals(expectedMessage, e.getMessage());

            // Verify that the parsing error was detected at the beginning of the string.
            assertEquals(0, e.getErrorOffset());
        }
    }
}