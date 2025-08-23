package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Test suite for the {@link LocaleUtils} class, focusing on exception handling.
 */
public class LocaleUtilsTest {

    /**
     * Tests that toLocale() throws an IllegalArgumentException when the input string
     * has an invalid format that cannot be parsed into a Locale.
     */
    @Test
    public void toLocale_shouldThrowIllegalArgumentException_forInvalidFormat() {
        // Arrange: An input string that does not conform to the expected locale format.
        final String invalidLocaleString = "#";

        // Act & Assert: Verify that the method throws the correct exception
        // with a descriptive message.
        try {
            LocaleUtils.toLocale(invalidLocaleString);
            fail("Expected an IllegalArgumentException to be thrown, but it wasn't.");
        } catch (final IllegalArgumentException e) {
            // Assert that the exception message clearly states the issue.
            final String expectedMessage = "Invalid locale format: " + invalidLocaleString;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}