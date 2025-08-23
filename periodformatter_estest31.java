package org.joda.time.format;

import org.joda.time.PeriodType;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link PeriodFormatter}.
 * This class focuses on parsing behavior with literal-based formatters.
 */
public class PeriodFormatterTest {

    /**
     * Tests that parseMutablePeriod throws an IllegalArgumentException when the input string
     * does not match the expected literal format.
     */
    @Test
    public void parseMutablePeriod_shouldThrowIllegalArgumentException_whenInputDoesNotMatchLiteral() {
        // Arrange: Create a formatter that only accepts a specific literal string.
        final String literalFormat = "PeriodFormat.months.list";
        PeriodFormatterBuilder.Literal literalComponent = new PeriodFormatterBuilder.Literal(literalFormat);

        // The formatter is configured to use the literal component for both printing and parsing.
        PeriodFormatter formatter = new PeriodFormatter(literalComponent, literalComponent, Locale.KOREA, (PeriodType) null);

        final String invalidInput = "v:";
        final String expectedErrorMessage = "Invalid format: \"" + invalidInput + "\"";

        // Act & Assert
        try {
            formatter.parseMutablePeriod(invalidInput);
            fail("Expected an IllegalArgumentException because the input string does not match the format.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message correctly identifies the invalid input.
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }
}