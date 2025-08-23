package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link StringConverter}.
 */
public class StringConverterTest {

    /**
     * Tests that getDurationMillis() throws an IllegalArgumentException
     * when provided with a string that does not conform to the ISO8601 duration format.
     */
    @Test
    public void getDurationMillis_shouldThrowIllegalArgumentException_forInvalidFormat() {
        // Arrange
        final StringConverter converter = StringConverter.INSTANCE;
        final String invalidDurationString = "~]TNU:z]";
        final String expectedMessage = "Invalid format: \"" + invalidDurationString + "\"";

        // Act & Assert
        try {
            converter.getDurationMillis(invalidDurationString);
            fail("Expected an IllegalArgumentException to be thrown for invalid format");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message is correct.
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}