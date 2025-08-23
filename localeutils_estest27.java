package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Locale;

/**
 * Unit tests for {@link org.apache.commons.lang3.LocaleUtils}.
 */
public class LocaleUtilsTest {

    /**
     * Tests that {@link LocaleUtils#languagesByCountry(String)} returns the correct
     * language for a known country code. For Romania ("RO"), it should return a list
     * containing the Romanian locale ("ro_RO").
     */
    @Test
    public void languagesByCountry_shouldReturnRomanianForRomania() {
        // Arrange: Define the input and the expected outcome.
        // The country code "RO" represents Romania.
        final String countryCodeRO = "RO";
        final Locale expectedLocale = new Locale("ro", "RO");

        // Act: Call the method under test.
        final List<Locale> actualLanguages = LocaleUtils.languagesByCountry(countryCodeRO);

        // Assert: Verify the result is as expected.
        assertNotNull("The returned list should not be null.", actualLanguages);
        assertEquals("There should be exactly one language for the country code 'RO'.", 1, actualLanguages.size());
        assertEquals("The language for 'RO' should be the Romanian locale.", expectedLocale, actualLanguages.get(0));
    }
}