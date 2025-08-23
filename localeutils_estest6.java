package org.apache.commons.lang3;

import org.junit.Test;
import java.util.Locale;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link LocaleUtils}.
 */
public class LocaleUtilsTest {

    /**
     * Tests that {@link LocaleUtils#toLocale(String)} can correctly parse a
     * string that starts with a separator, followed by a country and variant code.
     * The input "-DY-n" should result in a Locale with an empty language,
     * the country "DY", and the variant "n".
     */
    @Test
    public void toLocaleShouldParseStringWithLeadingSeparatorAndCountryAndVariant() {
        // Arrange
        final String localeString = "-DY-n";
        final String expectedLanguage = "";
        final String expectedCountry = "DY";
        final String expectedVariant = "n";

        // Act
        final Locale resultLocale = LocaleUtils.toLocale(localeString);

        // Assert
        assertEquals(expectedLanguage, resultLocale.getLanguage());
        assertEquals(expectedCountry, resultLocale.getCountry());
        assertEquals(expectedVariant, resultLocale.getVariant());
        
        // The toString() representation should be "_DY_n"
        assertEquals("_DY_n", resultLocale.toString());
    }
}