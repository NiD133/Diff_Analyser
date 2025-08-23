package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link StringConverter}.
 * This test focuses on parsing invalid duration strings.
 */
public class StringConverter_ESTestTest3 extends StringConverter_ESTest_scaffolding {

    /**
     * Tests that getDurationMillis() throws an IllegalArgumentException
     * for a string that does not conform to the ISO 8601 duration format.
     */
    @Test
    public void getDurationMillis_shouldThrowExceptionForInvalidFormat() {
        // Arrange
        final StringConverter converter = StringConverter.INSTANCE;
        final String invalidDurationString = "P vb";
        final String expectedErrorMessage = "Invalid format: \"" + invalidDurationString + "\"";

        try {
            // Act
            converter.getDurationMillis(invalidDurationString);
            fail("Expected an IllegalArgumentException to be thrown for invalid format.");
        } catch (IllegalArgumentException e) {
            // Assert
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }
}