package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("LocaleUtils Tests")
public class LocaleUtilsTest extends AbstractLangTest {

    /**
     * Checks that a collection is unmodifiable.
     *
     * @param coll the collection to check
     */
    private static void assertUnmodifiableCollection(final Collection<?> coll) {
        assertThrows(UnsupportedOperationException.class, () -> coll.add(null));
    }

    /**
     * Provides test cases for countries by language.
     * @return A stream of arguments: language code and an array of expected country codes.
     */
    static Stream<Arguments> provideLanguagesAndCountries() {
        return Stream.of(
            Arguments.of(null, new String[0]),
            Arguments.of("de", new String[]{"DE", "CH", "AT", "LU"}),
            Arguments.of("zz", new String[0]),
            Arguments.of("it", new String[]{"IT", "CH"})
        );
    }

    @BeforeEach
    public void setUp() {
        // Testing #LANG-304. Must be called before availableLocaleSet is called to ensure
        // the available locale list is initialized correctly.
        LocaleUtils.isAvailableLocale(Locale.getDefault());
    }

    @DisplayName("countriesByLanguage() should return expected countries for a given language")
    @ParameterizedTest(name = "Language: \"{0}\", Expected Countries: {1}")
    @MethodSource("provideLanguagesAndCountries")
    void countriesByLanguage_shouldReturnExpectedCountriesForLanguage(final String language, final String[] expectedCountries) {
        final List<Locale> locales = LocaleUtils.countriesByLanguage(language);
        assertNotNull(locales);

        // Check that the list is cached by ensuring the same instance is returned on a subsequent call.
        assertSame(locales, LocaleUtils.countriesByLanguage(language));

        // Check that the returned list is unmodifiable.
        assertUnmodifiableCollection(locales);

        // Check that all returned locales have the correct language and an empty variant.
        if (language != null) {
            locales.forEach(locale -> {
                assertEquals(language, locale.getLanguage(), "All locales in the list should have the correct language.");
                assertTrue(StringUtils.isEmpty(locale.getVariant()), "All locales in the list should have an empty variant.");
            });
        }

        // Check that the list contains at least the expected countries.
        // The list may contain more due to JVM variations.
        final Set<String> actualCountries = locales.stream().map(Locale::getCountry).collect(Collectors.toSet());
        final Set<String> expectedCountrySet = new HashSet<>(Arrays.asList(expectedCountries));

        assertTrue(actualCountries.containsAll(expectedCountrySet),
            () -> "Result for language '" + language + "' should contain " + expectedCountrySet + " but was " + actualCountries);
    }

    @DisplayName("toLocale(Locale) should return the same locale instance")
    @Test
    void toLocale_whenGivenNullLocale_shouldReturnNull() {
        assertNull(LocaleUtils.toLocale((Locale) null));
    }

    @DisplayName("toLocale(String) should correctly handle all available locales")
    @ParameterizedTest(name = "{0}")
    @MethodSource("java.util.Locale#getAvailableLocales")
    void toLocaleFromString_shouldCorrectlyHandleAllAvailableLocales(final Locale locale) {
        // A "simple" locale can be fully reconstructed from its language, country, and variant.
        // Locales with extensions (e.g., script, unicode extensions) cannot.
        final boolean isSimpleLocale = locale.equals(new Locale(locale.getLanguage(), locale.getCountry(), locale.getVariant()));
        final String localeString = locale.toString();

        if (isSimpleLocale) {
            // LocaleUtils.toLocale is expected to handle simple locales.
            // However, it does not support parsing suffixes for scripts or extensions.
            final int suffixIndex = localeString.indexOf("#");
            if (suffixIndex >= 0) {
                // This is a rare case where a "simple" locale's toString() has a suffix.
                // Our toLocale method does not support these suffixes and should fail.
                final String stringWithSuffix = localeString;
                assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(stringWithSuffix),
                    "Should fail for string with suffix: " + stringWithSuffix);

                // But it should parse correctly when the suffix is removed.
                final String strippedString = localeString.substring(0, suffixIndex);
                assertEquals(locale, LocaleUtils.toLocale(strippedString));
            } else {
                // Standard case: a simple locale string without suffixes should parse correctly.
                assertEquals(locale, LocaleUtils.toLocale(localeString));
            }
        } else {
            // For locales with extensions, toLocale(String) is expected to fail because it doesn't parse them.
            // This is documented behavior for the method.
            assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(localeString),
                "Should fail for locale string with extensions: " + localeString);
        }
    }

    @DisplayName("toLocale(Locale) should return the same locale instance for all available locales")
    @ParameterizedTest(name = "{0}")
    @MethodSource("java.util.Locale#getAvailableLocales")
    void toLocale_whenGivenLocale_shouldReturnSameInstance(final Locale actualLocale) {
        assertEquals(actualLocale, LocaleUtils.toLocale(actualLocale));
    }
}