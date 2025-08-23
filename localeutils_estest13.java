package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link org.apache.commons.lang3.LocaleUtils}.
 */
public class LocaleUtilsTest {

    /**
     * Tests that toLocale() throws an IllegalArgumentException when given a string
     * that does not conform to the expected locale format.
     */
    @Test
    public void toLocale_withInvalidFormatString_shouldThrowIllegalArgumentException() {
        // Arrange: Define an input string that is not a valid locale format.
        final String invalidLocaleString = "+";

        // Act & Assert
        try {
            LocaleUtils.toLocale(invalidLocaleString);
            fail("Expected an IllegalArgumentException for invalid format: " + invalidLocaleString);
        } catch (final IllegalArgumentException e) {
            // Verify that the exception message clearly indicates the problem.
            final String expectedMessage = "Invalid locale format: " + invalidLocaleString;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}