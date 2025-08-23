package org.apache.commons.lang3;

import org.junit.Test;
import java.util.Locale;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link org.apache.commons.lang3.LocaleUtils}.
 */
public class LocaleUtilsTest {

    /**
     * Tests that {@link LocaleUtils#toLocale(String)} can correctly parse a string
     * with a mixed-case language code, a dash separator, and a numeric area code for the country.
     *
     * The input "biTN-009" is expected to be parsed into a Locale with language "bitn"
     * and country "009". This test verifies that the parsing logic correctly handles
     * case normalization and separator type.
     */
    @Test
    public void toLocaleShouldParseStringWithMixedCaseLanguageAndNumericCountry() {
        // Arrange
        final String localeString = "biTN-009";
        final Locale expectedLocale = new Locale("bitn", "009");

        // Act
        final Locale actualLocale = LocaleUtils.toLocale(localeString);

        // Assert
        assertEquals(expectedLocale, actualLocale);
    }
}