package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link StringConverter}.
 */
public class StringConverterTest {

    @Test
    public void getDurationMillis_shouldThrowIllegalArgumentException_forInvalidPeriodFormat() {
        // Arrange
        StringConverter converter = StringConverter.INSTANCE;
        // The ISO8601 period format requires an uppercase 'T' as the time designator.
        String invalidPeriodString = "Pt-is";

        // Act & Assert
        try {
            converter.getDurationMillis(invalidPeriodString);
            fail("Expected an IllegalArgumentException to be thrown for invalid format.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message clearly indicates the problem.
            assertEquals("Invalid format: \"" + invalidPeriodString + "\"", e.getMessage());
        }
    }
}