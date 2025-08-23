package org.apache.commons.lang3;

import static org.junit.Assert.assertFalse;

import org.junit.Test;
import java.util.Locale;

/**
 * Unit tests for {@link org.apache.commons.lang3.LocaleUtils}.
 */
public class LocaleUtilsTest {

    /**
     * Tests that {@link LocaleUtils#isAvailableLocale(Locale)} returns false
     * for a null input, as the class is designed to handle nulls gracefully.
     */
    @Test
    public void isAvailableLocale_shouldReturnFalse_forNullInput() {
        // Arrange: No arrangement is needed as we are testing a null input.

        // Act: Call the method under test with a null locale.
        final boolean result = LocaleUtils.isAvailableLocale(null);

        // Assert: The method should return false.
        assertFalse("isAvailableLocale(null) should return false", result);
    }
}