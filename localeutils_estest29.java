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
     * that does not follow the expected locale format.
     */
    @Test
    public void toLocaleShouldThrowIllegalArgumentExceptionForMalformedString() {
        // Arrange: A string that does not conform to the valid locale format
        // (e.g., "en", "en_GB", "en_GB_xxx"). This string has an invalid language part.
        final String malformedLocaleString = "DX\"n{f!cA_1";

        // Act & Assert
        try {
            LocaleUtils.toLocale(malformedLocaleString);
            fail("Expected an IllegalArgumentException to be thrown for malformed input.");
        } catch (final IllegalArgumentException e) {
            // Verify that the exception message correctly identifies the invalid input.
            final String expectedMessage = "Invalid locale format: " + malformedLocaleString;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}