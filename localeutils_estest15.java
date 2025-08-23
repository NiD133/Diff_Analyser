package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;

import java.util.Locale;
import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.lang3.LocaleUtils}.
 */
public class LocaleUtilsTest {

    /**
     * Tests that calling toLocale() with an empty string returns a Locale
     * object representing the empty locale (empty language, country, and variant).
     */
    @Test
    public void toLocale_withEmptyString_shouldReturnEmptyLocale() {
        // Arrange
        final String emptyLocaleString = "";
        final Locale expectedLocale = new Locale("", "");

        // Act
        final Locale actualLocale = LocaleUtils.toLocale(emptyLocaleString);

        // Assert
        // The original test only verified the language. This is more thorough,
        // ensuring the entire Locale object matches the expected empty state.
        assertEquals(expectedLocale, actualLocale);
    }
}