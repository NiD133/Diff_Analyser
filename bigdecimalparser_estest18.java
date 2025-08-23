package com.fasterxml.jackson.core.io;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link BigDecimalParser}.
 */
public class BigDecimalParserTest {

    /**
     * Tests that parsing a char array containing only null characters, which is not a
     * valid number, results in a NumberFormatException.
     */
    @Test
    public void parse_whenGivenNullCharacters_shouldThrowNumberFormatException() {
        // Arrange: Create an input that is invalid for BigDecimal parsing.
        // An array of null characters is not a valid number representation.
        char[] invalidInput = new char[]{'\u0000', '\u0000', '\u0000'};

        try {
            // Act: Attempt to parse the invalid input.
            BigDecimalParser.parse(invalidInput);
            fail("A NumberFormatException was expected, but it was not thrown.");
        } catch (NumberFormatException e) {
            // Assert: Verify that the exception message is informative and correct.
            String expectedMessage = "Value \"\u0000\u0000\u0000\" can not be deserialized as `java.math.BigDecimal`, reason:  Not a valid number representation";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}