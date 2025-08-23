package org.joda.time.convert;

import org.joda.time.MutableInterval;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link StringConverter}.
 * This test focuses on how the converter handles invalid string formats for intervals.
 */
public class StringConverterTest {

    /**
     * Tests that the converter, when used by {@link MutableInterval}'s constructor,
     * throws an IllegalArgumentException for a string that does not represent a valid interval.
     * The ISO8601 standard for an interval string is {@code <start>/<end>}.
     */
    @Test
    public void setIntoInterval_withInvalidFormatString_shouldThrowIllegalArgumentException() {
        // Arrange: An interval string with a clearly invalid format.
        final String invalidIntervalString = "/UkrB+[Yx$";

        // Act & Assert
        try {
            // The MutableInterval constructor uses the conversion framework, which delegates
            // to StringConverter for string inputs.
            new MutableInterval(invalidIntervalString);
            fail("Expected an IllegalArgumentException for the invalid format.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message clearly states the format is invalid,
            // and includes the problematic string.
            assertEquals("Format invalid: " + invalidIntervalString, e.getMessage());
        }
    }
}