package org.apache.commons.lang3;

import org.junit.Test;
import java.util.Locale;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link LocaleUtils}.
 */
public class LocaleUtilsTest {

    /**
     * Tests that {@link LocaleUtils#isLanguageUndetermined(Locale)} returns true for {@link Locale#ROOT}.
     * The language tag for Locale.ROOT is "und" (undetermined) because its language component is an empty string.
     */
    @Test
    public void testIsLanguageUndetermined_withRootLocale_returnsTrue() {
        // Arrange: Locale.ROOT is a locale with an empty language, country, and variant.
        final Locale rootLocale = Locale.ROOT;

        // Act: Check if the language is undetermined.
        final boolean result = LocaleUtils.isLanguageUndetermined(rootLocale);

        // Assert: The result should be true.
        assertTrue("Locale.ROOT should be considered as having an undetermined language.", result);
    }
}