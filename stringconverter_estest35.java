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
     * when provided with a string that does not conform to the ISO 8601 duration format.
     */
    @Test
    public void getDurationMillis_shouldThrowIllegalArgumentException_forInvalidFormatString() {
        // Arrange
        final StringConverter converter = StringConverter.INSTANCE;
        final String invalidDurationString = "p3@QA9'OLT&K_7a]X<";

        // Act & Assert
        try {
            converter.getDurationMillis(invalidDurationString);
            fail("Expected an IllegalArgumentException to be thrown for an invalid duration format.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message is as expected.
            final String expectedMessage = "Invalid format: \"" + invalidDurationString + "\"";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}