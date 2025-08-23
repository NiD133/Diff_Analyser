package org.apache.commons.lang3;

import org.junit.Test;
import java.util.Locale;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for {@link org.apache.commons.lang3.LocaleUtils}.
 */
public class LocaleUtilsTest {

    /**
     * Tests that {@link LocaleUtils#toLocale(String)} can correctly parse a
     * string containing language, country, and variant components separated by dashes.
     */
    @Test
    public void toLocaleShouldParseLanguageCountryVariantStringWithDashes() {
        // Arrange
        final String localeStringWithDashes = "bfi-TN-D9";
        final String expectedLanguage = "bfi";
        final String expectedCountry = "TN";
        final String expectedVariant = "D9";
        final String expectedToString = "bfi_TN_D9";

        // Act
        final Locale actualLocale = LocaleUtils.toLocale(localeStringWithDashes);

        // Assert
        assertNotNull("The resulting locale should not be null.", actualLocale);
        assertEquals("The language code should be correctly parsed.", expectedLanguage, actualLocale.getLanguage());
        assertEquals("The country code should be correctly parsed.", expectedCountry, actualLocale.getCountry());
        assertEquals("The variant code should be correctly parsed.", expectedVariant, actualLocale.getVariant());
        assertEquals("The toString() representation should use underscores as separators.", expectedToString, actualLocale.toString());
    }
}