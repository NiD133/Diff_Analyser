package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link LocaleUtils}, focusing on locale parsing and lookups.
 */
public class LocaleUtilsTest extends AbstractLangTest {

    @BeforeEach
    public void setUp() {
        // Testing #LANG-304. This must be called before availableLocaleSet is used
        // to ensure the internal cache is initialized.
        LocaleUtils.isAvailableLocale(Locale.getDefault());
    }

    @DisplayName("toLocale(String) should correctly parse all available standard locales")
    @ParameterizedTest(name = "Parsing locale: {0}")
    @MethodSource("java.util.Locale#getAvailableLocales")
    void testToLocale_parsesAllAvailableStandardLocales(final Locale availableLocale) {
        // This test verifies that LocaleUtils.toLocale can handle all locales from
        // Locale.getAvailableLocales(), provided they don't have scripts or extensions,
        // which are not supported by toLocale(String).

        // Skips locales with scripts or extensions (e.g., "th_TH_TH_#u-nu-thai"),
        // as they cannot be fully represented by new Locale(lang, country, variant).
        final Locale standardLocale = new Locale(availableLocale.getLanguage(), availableLocale.getCountry(), availableLocale.getVariant());
        if (!availableLocale.equals(standardLocale)) {
            return; // This locale has features beyond what toLocale(String) supports.
        }

        final String localeString = availableLocale.toString();

        // LocaleUtils.toLocale does not support BCP 47 script or extension markers ("_#" or "#").
        // Check if the string representation contains them.
        int scriptMarkerIndex = localeString.indexOf("_#");
        if (scriptMarkerIndex == -1) {
            scriptMarkerIndex = localeString.indexOf("#");
        }

        if (scriptMarkerIndex >= 0) {
            // If a marker is present, toLocale should fail for the full string.
            assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(localeString),
                "toLocale should fail for strings with script/extension markers: " + localeString);

            // Test again with the unsupported suffix removed.
            final String parsableLocaleString = localeString.substring(0, scriptMarkerIndex);
            final Locale parsedLocale = LocaleUtils.toLocale(parsableLocaleString);
            assertEquals(availableLocale, parsedLocale,
                "toLocale should parse the locale correctly after removing the suffix.");
        } else {
            // If no marker is present, toLocale should parse it correctly.
            final Locale parsedLocale = LocaleUtils.toLocale(localeString);
            assertEquals(availableLocale, parsedLocale, "toLocale should correctly parse standard locale string.");
        }
    }

    @DisplayName("toLocale(Locale) should return the same locale instance")
    @ParameterizedTest(name = "Locale: {0}")
    @MethodSource("java.util.Locale#getAvailableLocales")
    void testToLocale_withLocaleArgument_returnsSameLocale(final Locale locale) {
        // The method is expected to be a null-safe pass-through for non-null locales.
        assertEquals(locale, LocaleUtils.toLocale(locale));
    }

    @Test
    @DisplayName("toLocale should correctly parse strings with only language and variant")
    void testToLocale_withLanguageAndVariant() {
        // This test addresses LANG-328, ensuring that locales with an empty country
        // but a non-empty variant are parsed correctly (e.g., "fr__P").
        assertAll("Locales with language and variant",
            () -> assertValidToLocale("fr__P", "fr", "", "P"),
            () -> assertValidToLocale("fr__POSIX", "fr", "", "POSIX")
        );
    }

    //-----------------------------------------------------------------------
    // Helper methods
    //-----------------------------------------------------------------------

    /**
     * Asserts that the list of countries returned for a given language is correct.
     * <p>
     * It verifies that {@link LocaleUtils#countriesByLanguage(String)} returns a list
     * containing at least the expected countries. The actual list may contain more
     * due to JVM variations.
     * </p>
     *
     * @param language the language code (e.g., "en")
     * @param expectedCountries the array of country codes that must be present
     */
    private static void assertCountriesByLanguage(final String language, final String[] expectedCountries) {
        final List<Locale> actualLocales = LocaleUtils.countriesByLanguage(language);
        final List<Locale> actualLocales2 = LocaleUtils.countriesByLanguage(language);

        assertNotNull(actualLocales, "The list of locales should not be null.");
        assertSame(actualLocales, actualLocales2, "Successive calls should return the same cached list instance.");

        // Verify that all returned locales have the correct language and an empty variant
        for (final Locale locale : actualLocales) {
            assertEquals(language, locale.getLanguage(), "All locales in the list should have the correct language.");
            assertTrue(StringUtils.isEmpty(locale.getVariant()), "All locales in the list should have an empty variant.");
        }

        // Extract country codes from the returned locales
        final Set<String> actualCountries = actualLocales.stream()
            .map(Locale::getCountry)
            .collect(Collectors.toSet());

        // Check if the list of actual countries contains all expected countries.
        final List<String> expected = Arrays.asList(expectedCountries);
        assertTrue(actualCountries.containsAll(expected),
            () -> "List of countries for language '" + language + "' was expected to contain " + expected +
                  " but was " + actualCountries);

        assertUnmodifiableCollection(actualLocales);
    }

    /**
     * Asserts that the list of languages returned for a given country is correct.
     * <p>
     * It verifies that {@link LocaleUtils#languagesByCountry(String)} returns a list
     * containing at least the expected languages. The actual list may contain more
     * due to JVM variations.
     * </p>
     *
* @param country the country code (e.g., "US")
     * @param expectedLanguages the array of language codes that must be present
     */
    private static void assertLanguageByCountry(final String country, final String[] expectedLanguages) {
        final List<Locale> actualLocales = LocaleUtils.languagesByCountry(country);
        final List<Locale> actualLocales2 = LocaleUtils.languagesByCountry(country);

        assertNotNull(actualLocales, "The list of locales should not be null.");
        assertSame(actualLocales, actualLocales2, "Successive calls should return the same cached list instance.");

        for (final Locale locale : actualLocales) {
            assertEquals(country, locale.getCountry(), "All locales in the list should have the correct country.");
            assertTrue(StringUtils.isEmpty(locale.getVariant()), "All locales in the list should have an empty variant.");
        }

        final Set<String> actualLanguages = actualLocales.stream()
            .map(Locale::getLanguage)
            .collect(Collectors.toSet());

        final List<String> expected = Arrays.asList(expectedLanguages);
        assertTrue(actualLanguages.containsAll(expected),
            () -> "List of languages for country '" + country + "' was expected to contain " + expected +
                  " but was " + actualLanguages);

        assertUnmodifiableCollection(actualLocales);
    }

    /**
     * Asserts that the locale lookup list is as expected.
     *
     * @param locale the input locale
     * @param defaultLocale the input default locale (can be null)
     * @param expected the expected list of locales
     */
    private static void assertLocaleLookupList(final Locale locale, final Locale defaultLocale, final Locale[] expected) {
        final List<Locale> localeList = defaultLocale == null
            ? LocaleUtils.localeLookupList(locale)
            : LocaleUtils.localeLookupList(locale, defaultLocale);

        assertEquals(expected.length, localeList.size());
        assertEquals(Arrays.asList(expected), localeList);
        assertUnmodifiableCollection(localeList);
    }

    /**
     * Asserts that a collection is unmodifiable by trying to add an element to it.
     *
     * @param coll the collection to check
     */
    private static void assertUnmodifiableCollection(final Collection<?> coll) {
        assertThrows(UnsupportedOperationException.class, () -> coll.add(null));
    }

    /**
     * Asserts that {@code LocaleUtils.toLocale(language)} works for a simple language string.
     *
     * @param language the language string (e.g., "en")
     */
    private static void assertValidToLocale(final String language) {
        final Locale locale = LocaleUtils.toLocale(language);
        assertNotNull(locale, "valid locale");
        assertEquals(language, locale.getLanguage());
        assertTrue(StringUtils.isEmpty(locale.getCountry()), "Country should be empty");
        assertTrue(StringUtils.isEmpty(locale.getVariant()), "Variant should be empty");
    }

    /**
     * Asserts that {@code LocaleUtils.toLocale(localeString)} correctly parses language and country.
     *
     * @param localeString the string to parse (e.g., "en_US")
     * @param language the expected language
     * @param country the expected country
     */
    private static void assertValidToLocale(final String localeString, final String language, final String country) {
        final Locale locale = LocaleUtils.toLocale(localeString);
        assertNotNull(locale, "valid locale");
        assertEquals(language, locale.getLanguage());
        assertEquals(country, locale.getCountry());
        assertTrue(StringUtils.isEmpty(locale.getVariant()), "Variant should be empty");
    }

    /**
     * Asserts that {@code LocaleUtils.toLocale(localeString)} correctly parses language, country, and variant.
     *
     * @param localeString the string to parse (e.g., "en_US_WIN")
     * @param language the expected language
     * @param country the expected country
     * @param variant the expected variant
     */
    private static void assertValidToLocale(final String localeString, final String language, final String country, final String variant) {
        final Locale locale = LocaleUtils.toLocale(localeString);
        assertNotNull(locale, "valid locale");
        assertEquals(language, locale.getLanguage());
        assertEquals(country, locale.getCountry());
        assertEquals(variant, locale.getVariant());
    }
}