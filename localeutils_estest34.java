package org.apache.commons.lang3;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link LocaleUtils}.
 */
public class LocaleUtilsTest {

    /**
     * Tests that {@link LocaleUtils#toLocale(String)} throws an IllegalArgumentException
     * when given a string with an invalid format.
     *
     * <p>The input "bi-K-D9" is invalid because the country code part "K" is only
     * a single character, whereas it should be two uppercase letters or three digits.</p>
     */
    @Test
    public void toLocale_shouldThrowIllegalArgumentException_forInvalidLocaleFormat() {
        // Arrange: Define an invalid locale string.
        final String invalidLocaleString = "bi-K-D9";

        // Act & Assert: Verify that toLocale throws the expected exception.
        try {
            LocaleUtils.toLocale(invalidLocaleString);
            fail("Expected an IllegalArgumentException to be thrown for invalid format.");
        } catch (final IllegalArgumentException e) {
            // Assert: Check if the exception message is correct.
            final String expectedMessage = "Invalid locale format: " + invalidLocaleString;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}