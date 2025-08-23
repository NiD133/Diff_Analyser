package com.google.gson.internal.bind.util;

import org.junit.Test;

import java.text.ParseException;
import java.text.ParsePosition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link ISO8601Utils}.
 */
public class ISO8601UtilsTest {

    /**
     * Verifies that parsing a string with an invalid numeric component
     * throws a ParseException with a descriptive message.
     */
    @Test
    public void parse_withInvalidNumericComponent_throwsParseException() {
        // Arrange
        String invalidDateString = "p;72<&YyPko{%";
        // The test will start parsing from index 1, attempting to read ";72<" as a year.
        ParsePosition position = new ParsePosition(1);

        // Act & Assert
        try {
            ISO8601Utils.parse(invalidDateString, position);
            fail("A ParseException was expected but was not thrown.");
        } catch (ParseException e) {
            // The parser should fail because it expects a number but finds an invalid sequence.
            String expectedMessage = "Failed to parse date [\"" + invalidDateString + "\"]: Invalid number: ;72<";
            
            assertEquals("The exception message should detail the parsing failure.", expectedMessage, e.getMessage());
            assertEquals("The error offset should point to the start of the invalid token.", 1, e.getErrorOffset());
        }
    }
}