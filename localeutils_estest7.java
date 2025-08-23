package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link LocaleUtils}.
 */
public class LocaleUtilsTest {

    /**
     * Tests that toLocale() throws an IllegalArgumentException for a string
     * that starts with a separator ('_'), which is an invalid format.
     */
    @Test
    public void toLocale_shouldThrowIllegalArgumentException_forInvalidFormatStartingWithSeparator() {
        // Arrange: Define an invalid locale string and the expected exception message.
        final String invalidLocaleString = "_HUP";
        final String expectedMessage = "Invalid locale format: " + invalidLocaleString;

        // Act & Assert: Verify that calling the method with the invalid string
        // throws the correct exception with the expected message.
        try {
            LocaleUtils.toLocale(invalidLocaleString);
            fail("Expected an IllegalArgumentException to be thrown for invalid format.");
        } catch (final IllegalArgumentException e) {
            // Verify that the exception message is what we expect.
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}