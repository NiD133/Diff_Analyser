package com.google.gson.internal.bind.util;

import org.junit.Test;
import java.text.ParseException;
import java.text.ParsePosition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link ISO8601Utils}.
 * This class focuses on testing the parsing of date strings.
 */
public class ISO8601UtilsTest {

    /**
     * Tests that the {@code parse} method throws a {@link ParseException}
     * when given a date string that is fundamentally malformed and cannot be parsed as a number.
     */
    @Test
    public void parse_withMalformedDateString_shouldThrowParseException() {
        // Arrange
        // This string is malformed because it starts with a digit but is immediately
        // followed by non-digit characters, making it an invalid number for any date component (e.g., year).
        String malformedDateString = "9>({Cf/Td,\";U)Ji*";
        ParsePosition position = new ParsePosition(0);

        // Act & Assert
        try {
            ISO8601Utils.parse(malformedDateString, position);
            fail("A ParseException should have been thrown for the malformed date string.");
        } catch (ParseException e) {
            // Verify that the exception message is informative and contains the root cause.
            String expectedMessage = "Failed to parse date [\"" + malformedDateString + "\"]: Invalid number: 9>({";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}