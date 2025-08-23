package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link LocaleUtils}.
 */
public class LocaleUtilsTest {

    /**
     * Tests that toLocale() throws an IllegalArgumentException when given a string
     * that does not conform to the expected "language_country_variant" format.
     */
    @Test
    public void toLocaleShouldThrowIllegalArgumentExceptionForInvalidFormat() {
        // Arrange: Define an input string that is not a valid locale format.
        // The string "íslenska" is the Icelandic word for "Icelandic".
        final String invalidLocaleString = "\u00EDslenska";
        final String expectedMessage = "Invalid locale format: " + invalidLocaleString;

        // Act & Assert
        try {
            LocaleUtils.toLocale(invalidLocaleString);
            fail("Expected an IllegalArgumentException to be thrown for invalid format.");
        } catch (final IllegalArgumentException e) {
            // Verify that the exception message is correct, as it provides
            // useful feedback to the developer using the LocaleUtils class.
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}