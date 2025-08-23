package org.apache.commons.lang3;

import org.junit.Test;
import java.util.Locale;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link LocaleUtils} class.
 */
public class LocaleUtilsTest {

    /**
     * Tests that {@link LocaleUtils#isAvailableLocale(Locale)} returns true
     * for a locale that is part of the standard set of available locales.
     */
    @Test
    public void isAvailableLocale_shouldReturnTrue_forStandardLocale() {
        // Arrange: Define a locale that is expected to be available in any standard Java environment.
        // Locale.ITALY is a constant and a reliable choice for this test.
        final Locale standardLocale = Locale.ITALY;

        // Act: Call the method under test.
        final boolean isAvailable = LocaleUtils.isAvailableLocale(standardLocale);

        // Assert: Verify that the method correctly identifies the locale as available.
        assertTrue("Locale.ITALY should be recognized as an available locale.", isAvailable);
    }
}