package com.fasterxml.jackson.core.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link BigDecimalParser}, focusing on its error handling capabilities,
 * particularly for very long and invalid input strings.
 */
class BigDecimalParserTest extends com.fasterxml.jackson.core.JUnit5TestBase {

    // Define a length that exceeds the parser's reporting limit to test truncation logic.
    // See BigDecimalParser.MAX_CHARS_TO_REPORT
    private static final int LONG_INVALID_STRING_LENGTH = BigDecimalParser.MAX_CHARS_TO_REPORT + 500;

    /**
     * Verifies that parsing an invalid string that is longer than the internal
     * reporting limit throws a NumberFormatException with a truncated message.
     * This ensures that error messages for excessively long inputs remain manageable.
     */
    @Test
    @DisplayName("Should throw exception with truncated message for long invalid string")
    void parseLongInvalidString() {
        // Arrange: Create an invalid input string that is intentionally longer
        // than the parser's exception message reporting limit.
        final String longInvalidInput = generateLongInvalidString();

        // Act & Assert: Verify that parsing this string throws a NumberFormatException.
        NumberFormatException exception = assertThrows(NumberFormatException.class, () -> {
            BigDecimalParser.parse(longInvalidInput);
        });

        // Assert: Check that the exception's message indicates truncation.
        String message = exception.getMessage();
        assertNotNull(message, "Exception message should not be null");
        assertTrue(
            message.startsWith("Value \"AAAA"),
            "Exception message should start with the problematic value."
        );
        assertTrue(
            message.contains("(truncated)"),
            "Exception message should explicitly state that the value was truncated."
        );
    }

    /**
     * Generates a long string composed of non-numeric characters ('A')
     * to serve as invalid input for the parser.
     *
     * @return A long, invalid string.
     */
    private static String generateLongInvalidString() {
        // In Java 11+, this could be: return "A".repeat(LONG_INVALID_STRING_LENGTH);
        final StringBuilder sb = new StringBuilder(LONG_INVALID_STRING_LENGTH);
        for (int i = 0; i < LONG_INVALID_STRING_LENGTH; i++) {
            sb.append('A');
        }
        return sb.toString();
    }
}