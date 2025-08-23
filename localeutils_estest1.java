package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Unit tests for {@link LocaleUtils}.
 */
public class LocaleUtilsTest {

    /**
     * Tests that localeLookupList generates a list of locales from the most
     * specific (language, country, variant) to the least specific (language only),
     * finally including the provided default locale.
     */
    @Test
    public void testLocaleLookupListWithLanguageCountryVariantAndDefault() {
        // Arrange: Define a specific locale and a default locale.
        final Locale locale = new Locale("fr", "CA", "WIN");
        final Locale defaultLocale = Locale.GERMAN;

        // The expected list should contain the locales in a specific order of fallback.
        final List<Locale> expectedLookupList = Arrays.asList(
            new Locale("fr", "CA", "WIN"), // 1. Full locale
            new Locale("fr", "CA"),       // 2. Language and country
            new Locale("fr"),             // 3. Language only
            Locale.GERMAN                 // 4. Default locale
        );

        // Act: Call the method under test.
        final List<Locale> actualLookupList = LocaleUtils.localeLookupList(locale, defaultLocale);

        // Assert: Verify that the actual list matches the expected list in content and order.
        assertEquals(expectedLookupList, actualLookupList);
    }
}