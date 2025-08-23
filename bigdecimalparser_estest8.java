package com.fasterxml.jackson.core.io;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link BigDecimalParser} class.
 */
public class BigDecimalParserTest {

    /**
     * Verifies that attempting to parse an empty character segment (where length is zero)
     * results in a {@link NumberFormatException}, as it's not a valid number.
     */
    @Test
    public void parseShouldThrowNumberFormatExceptionForEmptyInput() {
        // Arrange: Define an empty segment of a character array.
        // The content of the array is irrelevant; the zero length is what's being tested.
        char[] inputChars = new char[1];
        int offset = 0;
        int length = 0;

        // Act & Assert
        try {
            BigDecimalParser.parse(inputChars, offset, length);
            fail("A NumberFormatException was expected, but it was not thrown.");
        } catch (NumberFormatException e) {
            // Verify that the exception message clearly indicates an empty value problem.
            final String expectedMessage = "Value \"\" can not be deserialized as `java.math.BigDecimal`, " +
                    "reason:  Not a valid number representation";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}