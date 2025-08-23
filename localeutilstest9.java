package org.apache.commons.lang3;

import static org.apache.commons.lang3.LangAssertions.assertIllegalArgumentException;
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
 * Tests for {@link LocaleUtils}.
 */
public class LocaleUtilsTest extends AbstractLangTest {

    /**
     * Asserts that the list of countries returned for a language is correct.
     * <p>
     * It checks that {@link LocaleUtils#countriesByLanguage(String)} returns a list
     * containing locales for all expected countries. The actual list may contain more
     * countries due to JVM variations.
     * </p>
     *
     * @param language        the language code to test.
     * @param expectedCountries an array of country codes that must be in the result.
     */
    private static void assertCountriesByLanguage(final String language, final String[] expectedCountries) {
        final List<Locale> actualLocales = LocaleUtils.countriesByLanguage(language);
        assertNotNull(actualLocales, "The list of locales should not be null.");

        // Check that the method returns a cached, unmodifiable list
        assertSame(actualLocales, LocaleUtils.countriesByLanguage(language), "Successive calls should return the same cached list instance.");
        assertUnmodifiableCollection(actualLocales);

        // Check that all returned locales have the correct language and an empty variant
        for (final Locale locale : actualLocales) {
            assertEquals(language, locale.getLanguage(), "All locales in the list should have the correct language.");
            assertTrue(StringUtils.isEmpty(locale.getVariant()), "All locales in the list should have an empty variant.");
        }

        // Check that the list contains all expected countries.
        final Set<String> actualCountries = actualLocales.stream()
            .map(Locale::getCountry)
            .collect(Collectors.toSet());

        for (final String expectedCountry : expectedCountries) {
            assertTrue(actualCountries.contains(expectedCountry),
                () -> "Did not find expected country '" + expectedCountry + "' for language '" + language + "'. Found: " + actualCountries);
        }
    }

    /**
     * Asserts that the list of languages returned for a country is correct.
     * <p>
     * It checks that {@link LocaleUtils#languagesByCountry(String)} returns a list
     * containing locales for all expected languages. The actual list may contain more
     * languages due to JVM variations.
     * </p>
     *
     * @param country         the country code to test.
     * @param expectedLanguages an array of language codes that must be in the result.
     */
    private static void assertLanguageByCountry(final String country, final String[] expectedLanguages) {
        final List<Locale> actualLocales = LocaleUtils.languagesByCountry(country);
        assertNotNull(actualLocales, "The list of locales should not be null.");

        // Check that the method returns a cached, unmodifiable list
        assertSame(actualLocales, LocaleUtils.languagesByCountry(country), "Successive calls should return the same cached list instance.");
        assertUnmodifiableCollection(actualLocales);

        // Check that all returned locales have the correct country and an empty variant
        for (final Locale locale : actualLocales) {
            assertEquals(country, locale.getCountry(), "All locales in the list should have the correct country.");
            assertTrue(StringUtils.isEmpty(locale.getVariant()), "All locales in the list should have an empty variant.");
        }

        // Check that the list contains all expected languages.
        final Set<String> actualLanguages = actualLocales.stream()
            .map(Locale::getLanguage)
            .collect(Collectors.toSet());

        for (final String expectedLanguage : expectedLanguages) {
            assertTrue(actualLanguages.contains(expectedLanguage),
                () -> "Did not find expected language '" + expectedLanguage + "' for country '" + country + "'. Found: " + actualLanguages);
        }
    }

    /**
     * Helper to assert that a collection is unmodifiable.
     */
    private static void assertUnmodifiableCollection(final Collection<?> coll) {
        assertThrows(UnsupportedOperationException.class, () -> coll.add(null));
    }

    /**
     * Helper to assert that a locale string is parsed correctly.
     */
    private static void assertValidToLocale(final String localeString, final String language, final String country, final String variant) {
        final Locale locale = LocaleUtils.toLocale(localeString);
        assertNotNull(locale, "valid locale");
        assertEquals(language, locale.getLanguage());
        assertEquals(country, locale.getCountry());
        assertEquals(variant, locale.getVariant());
    }

    /**
     * Overloaded helper for locales without a variant.
     */
    private static void assertValidToLocale(final String localeString, final String language, final String country) {
        final Locale locale = LocaleUtils.toLocale(localeString);
        assertNotNull(locale, "valid locale");
        assertEquals(language, locale.getLanguage());
        assertEquals(country, locale.getCountry());
        assertTrue(StringUtils.isEmpty(locale.getVariant()), "Variant should be empty");
    }

    @BeforeEach
    public void setUp() {
        // Testing #LANG-304. Must be called before availableLocaleSet is called to ensure the cache is properly initialized.
        LocaleUtils.isAvailableLocale(Locale.getDefault());
    }

    @DisplayName("Test toLocale(String) with all available locales from the JVM")
    @ParameterizedTest(name = "[{index}] locale = {0}")
    @MethodSource("java.util.Locale#getAvailableLocales")
    void toLocale_withAllAvailableJvmLocales_parsesCorrectly(final Locale availableLocale) {
        // This test ensures that any locale available on the system can be successfully
        // parsed by LocaleUtils.toLocale(String), provided it doesn't have script or extension tags.

        // Some locales (e.g., with scripts) cannot be recreated with the simple
        // new Locale(lang, country, variant) constructor. We skip those as they are outside
        // the scope of what LocaleUtils.toLocale is designed to handle.
        final Locale simpleLocale = new Locale(availableLocale.getLanguage(), availableLocale.getCountry(), availableLocale.getVariant());
        if (!availableLocale.equals(simpleLocale)) {
            return; // Skip locales that can't be represented by the simple constructor.
        }

        final String localeString = availableLocale.toString();

        // LocaleUtils.toLocale does not support extensions (e.g., "_#u-co-phonebk").
        // First, find if an extension separator exists.
        int extensionSeparatorIndex = localeString.indexOf("_#");
        if (extensionSeparatorIndex == -1) {
            extensionSeparatorIndex = localeString.indexOf("#");
        }

        if (extensionSeparatorIndex >= 0) {
            // If an extension exists, verify that toLocale throws an exception for the full string.
            assertIllegalArgumentException(() -> LocaleUtils.toLocale(localeString),
                "toLocale should fail for strings with script/extension suffixes.");

            // Then, test parsing by stripping the extension and asserting equality.
            final String baseLocaleString = localeString.substring(0, extensionSeparatorIndex);
            final Locale parsedLocale = LocaleUtils.toLocale(baseLocaleString);
            assertEquals(availableLocale, parsedLocale);
        } else {
            // If no extension exists, parse the full string directly.
            final Locale parsedLocale = LocaleUtils.toLocale(localeString);
            assertEquals(availableLocale, parsedLocale);
        }
    }

    @DisplayName("Test toLocale(Locale) returns the same locale instance")
    @ParameterizedTest(name = "[{index}] locale = {0}")
    @MethodSource("java.util.Locale#getAvailableLocales")
    void toLocale_withLocaleArgument_returnsEqualLocale(final Locale actualLocale) {
        assertEquals(actualLocale, LocaleUtils.toLocale(actualLocale));
    }

    @Test
    @DisplayName("Test toLocale(String) with UN M.49 numeric area codes (LANG-1312)")
    void toLocale_withUnM49NumericAreaCodes_parsesCorrectly() {
        assertValidToLocale("en_001", "en", "001");
        assertValidToLocale("en_150", "en", "150");
        assertValidToLocale("ar_001", "ar", "001");
        // Test cases for LANG-1312
        assertValidToLocale("en_001_GB", "en", "001", "GB");
        assertValidToLocale("en_150_US", "en", "150", "US");
    }
}