package org.apache.commons.lang3;

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
 * Tests for {@link LocaleUtils}, focusing on parsing and handling of locales,
 * especially those available on the current JVM.
 */
public class LocaleUtilsAvailableLocalesTest extends AbstractLangTest {

    private static final Locale LOCALE_EN = new Locale("en", "");
    private static final Locale LOCALE_EN_US = new Locale("en", "US");
    private static final Locale LOCALE_EN_US_ZZZZ = new Locale("en", "US", "ZZZZ");
    private static final Locale LOCALE_FR = new Locale("fr", "");
    private static final Locale LOCALE_FR_CA = new Locale("fr", "CA");
    private static final Locale LOCALE_QQ = new Locale("qq", "");
    private static final Locale LOCALE_QQ_ZZ = new Locale("qq", "ZZ");

    /**
     * Asserts that the list of countries returned for a language is correct.
     * <p>
     * It checks that {@link LocaleUtils#countriesByLanguage(String)} returns a list
     * containing the expected countries. The actual list may contain more countries
     * due to JVM variations.
     * </p>
     *
     * @param language        the language code to test.
     * @param expectedCountries an array of country codes that must be in the result.
     */
    private static void assertCountriesByLanguage(final String language, final String[] expectedCountries) {
        final List<Locale> actualLocales = LocaleUtils.countriesByLanguage(language);
        final List<Locale> actualLocales2 = LocaleUtils.countriesByLanguage(language);

        assertNotNull(actualLocales);
        assertSame(actualLocales, actualLocales2, "countriesByLanguage should return a cached list");

        // The returned list may contain more countries than expected, so we check if our
        // expected countries are a subset of the actual ones.
        final Set<String> actualCountries = actualLocales.stream()
            .map(Locale::getCountry)
            .collect(Collectors.toSet());

        for (final String expectedCountry : expectedCountries) {
            assertTrue(actualCountries.contains(expectedCountry),
                () -> "List for language '" + language + "' should contain country '" + expectedCountry + "' but only contains " + actualCountries);
        }

        // Also verify that all returned locales have the correct language and an empty variant.
        for (final Locale locale : actualLocales) {
            assertEquals(language, locale.getLanguage(), "All locales in the list should have the correct language");
            assertTrue(StringUtils.isEmpty(locale.getVariant()), "All locales in the list should have an empty variant");
        }

        assertUnmodifiableCollection(actualLocales);
    }

    /**
     * Asserts that the list of languages returned for a country is correct.
     * <p>
     * It checks that {@link LocaleUtils#languagesByCountry(String)} returns a list
     * containing the expected languages. The actual list may contain more languages
     * due to JVM variations.
     * </p>
     *
     * @param country         the country code to test.
     * @param expectedLanguages an array of language codes that must be in the result.
     */
    private static void assertLanguageByCountry(final String country, final String[] expectedLanguages) {
        final List<Locale> actualLocales = LocaleUtils.languagesByCountry(country);
        final List<Locale> actualLocales2 = LocaleUtils.languagesByCountry(country);

        assertNotNull(actualLocales);
        assertSame(actualLocales, actualLocales2, "languagesByCountry should return a cached list");

        // The returned list may contain more languages than expected, so we check if our
        // expected languages are a subset of the actual ones.
        final Set<String> actualLanguages = actualLocales.stream()
            .map(Locale::getLanguage)
            .collect(Collectors.toSet());

        for (final String expectedLanguage : expectedLanguages) {
            assertTrue(actualLanguages.contains(expectedLanguage),
                () -> "List for country '" + country + "' should contain language '" + expectedLanguage + "' but only contains " + actualLanguages);
        }

        // Also verify that all returned locales have the correct country and an empty variant.
        for (final Locale locale : actualLocales) {
            assertEquals(country, locale.getCountry(), "All locales in the list should have the correct country");
            assertTrue(StringUtils.isEmpty(locale.getVariant()), "All locales in the list should have an empty variant");
        }

        assertUnmodifiableCollection(actualLocales);
    }

    /**
     * Helper method for testing locale lookup lists.
     */
    private static void assertLocaleLookupList(final Locale locale, final Locale defaultLocale, final Locale[] expected) {
        final List<Locale> localeList = defaultLocale == null ? LocaleUtils.localeLookupList(locale) : LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(expected.length, localeList.size());
        assertEquals(Arrays.asList(expected), localeList);
        assertUnmodifiableCollection(localeList);
    }

    /**
     * Asserts that the given collection is unmodifiable.
     */
    private static void assertUnmodifiableCollection(final Collection<?> coll) {
        assertThrows(UnsupportedOperationException.class, () -> coll.add(null));
    }

    @BeforeEach
    public void setUp() {
        // Testing #LANG-304. Must be called before availableLocaleSet is called.
        LocaleUtils.isAvailableLocale(Locale.getDefault());
    }

    @DisplayName("Test LocaleUtils.toLocale(String) with all available system locales")
    @ParameterizedTest(name = "[{index}] locale = {0}")
    @MethodSource("java.util.Locale#getAvailableLocales")
    void toLocale_handlesStringRepresentationOfAvailableLocales(final Locale availableLocale) {
        // This test is only valid for locales that can be fully represented by the
        // Locale(language, country, variant) constructor. Modern locales with scripts
        // or extensions (e.g., "sr_Latn_RS") are not supported by the SUT's toLocale(String)
        // and are filtered out by this check.
        final Locale simpleLocale = new Locale(availableLocale.getLanguage(), availableLocale.getCountry(), availableLocale.getVariant());
        if (!availableLocale.equals(simpleLocale)) {
            // Skips locales with scripts or extensions that can't be recreated simply.
            // For example, on Java 9+, Locale.forLanguageTag("sr-Latn-RS") creates a Locale
            // with a script, which would fail the .equals() check above.
            return;
        }

        final String localeString = availableLocale.toString();

        // LocaleUtils.toLocale is documented to not support the '#' character, which is used
        // in some legacy locale string representations as a variant or extension separator.
        if (localeString.contains("#")) {
            // If the string representation contains '#', toLocale should fail.
            assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(localeString),
                () -> "toLocale should fail for string with '#': " + localeString);
        } else {
            // For simple locales without '#', toLocale should parse the string representation back to the original Locale.
            final Locale parsedLocale = LocaleUtils.toLocale(localeString);
            assertEquals(availableLocale, parsedLocale,
                () -> "toLocale should correctly parse: " + localeString);
        }
    }

    @DisplayName("Test LocaleUtils.toLocale(Locale) returns the input locale")
    @ParameterizedTest(name = "[{index}] locale = {0}")
    @MethodSource("java.util.Locale#getAvailableLocales")
    void toLocale_withLocaleArgument_shouldReturnInputLocale(final Locale actualLocale) {
        assertEquals(actualLocale, LocaleUtils.toLocale(actualLocale));
    }

    @Test
    @DisplayName("Test toLocale(String) supports three-character language codes")
    void toLocale_shouldSupportThreeCharacterLanguageCodes() {
        for (final String langCode : Arrays.asList("udm", "tet")) {
            final Locale locale = LocaleUtils.toLocale(langCode);
            assertNotNull(locale);
            assertEquals(langCode, locale.getLanguage());
            assertTrue(StringUtils.isBlank(locale.getCountry()));
            assertEquals(new Locale(langCode), locale);
        }
    }
}