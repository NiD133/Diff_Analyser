package org.apache.commons.lang3;

import org.junit.Test;
import java.util.Locale;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link LocaleUtils}.
 */
public class LocaleUtilsTest {

    /**
     * Tests that {@link LocaleUtils#toLocale(String)} correctly converts a
     * string containing only a two-letter language code.
     */
    @Test
    public void toLocale_withLanguageCodeOnly_returnsCorrectLocale() {
        // Arrange
        final String localeString = "fr";
        final Locale expectedLocale = new Locale("fr");

        // Act
        final Locale actualLocale = LocaleUtils.toLocale(localeString);

        // Assert
        assertEquals(expectedLocale, actualLocale);
    }
}