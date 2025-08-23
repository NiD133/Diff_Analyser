package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link LocaleUtils}.
 */
public class LocaleUtilsTest {

    /**
     * Tests that {@link LocaleUtils#toLocale(String)} throws an IllegalArgumentException
     * when the input string does not conform to the expected locale format.
     */
    @Test
    public void toLocale_withInvalidFormatString_shouldThrowIllegalArgumentException() {
        // Arrange
        final String invalidLocaleString = "-C!N";

        // Act & Assert
        try {
            LocaleUtils.toLocale(invalidLocaleString);
            fail("Expected an IllegalArgumentException to be thrown for invalid format.");
        } catch (final IllegalArgumentException e) {
            // Assert that the exception message correctly identifies the invalid input.
            final String expectedMessage = "Invalid locale format: " + invalidLocaleString;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}