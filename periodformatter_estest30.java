package org.joda.time.format;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the PeriodFormatter class, focusing on parsing behavior.
 */
public class PeriodFormatterTest {

    /**
     * Verifies that attempting to parse a non-empty string using a formatter
     * that is configured to expect an empty string results in an IllegalArgumentException.
     */
    @Test
    public void parseMutablePeriod_withEmptyFormatter_throwsExceptionForNonEmptyInput() {
        // Arrange: Create a formatter that only accepts an empty string for parsing.
        // PeriodFormatterBuilder.Literal.EMPTY serves as a parser that expects no text.
        PeriodParser emptyParser = PeriodFormatterBuilder.Literal.EMPTY;
        PeriodFormatter emptyFormatter = new PeriodFormatter(null, emptyParser);
        final String invalidInput = "MIT";

        // Act & Assert
        try {
            emptyFormatter.parseMutablePeriod(invalidInput);
            fail("Expected an IllegalArgumentException because the input string is not empty.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message correctly identifies the invalid format.
            String expectedMessage = "Invalid format: \"" + invalidInput + "\"";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}