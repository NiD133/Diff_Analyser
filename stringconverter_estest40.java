package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the duration parsing functionality of {@link StringConverter}.
 */
public class StringConverterTest {

    /**
     * Tests that getDurationMillis() throws an IllegalArgumentException when given a string
     * in an ISO 8601 interval format, as it expects a duration format.
     */
    @Test
    public void getDurationMillis_shouldThrowException_forInvalidIntervalFormatString() {
        // Arrange
        StringConverter converter = new StringConverter();
        // This string represents an ISO 8601 interval, not a duration.
        String invalidIntervalString = "6/P7m";

        // Act & Assert
        try {
            converter.getDurationMillis(invalidIntervalString);
            fail("Expected an IllegalArgumentException to be thrown for invalid format.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message correctly identifies the invalid format.
            assertEquals("Invalid format: \"" + invalidIntervalString + "\"", e.getMessage());
        }
    }
}