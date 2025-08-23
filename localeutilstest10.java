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

@DisplayName("Tests for LocaleUtils")
public class LocaleUtilsTest extends AbstractLangTest {

    /**
     * Asserts that the given collection is unmodifiable by trying to add an element to it.
     *
     * @param coll the collection to check
     */
    private static void assertUnmodifiableCollection(final Collection<?> coll) {
        assertThrows(UnsupportedOperationException.class, () -> coll.add(null),
            "The collection should be unmodifiable.");
    }

    /**
     * Asserts that the result of {@link LocaleUtils#languagesByCountry(String)} is correct.
     * <p>
     * It checks that the returned list:
     * <ul>
     *     <li>Is not null and is cached.</li>
     *     <li>Is unmodifiable.</li>
     *     <li>Contains locales with the correct country and no variant.</li>
     *     <li>Contains at least the set of expected languages (may contain more due to JVM variations).</li>
     * </ul>
     *
     * @param countryCode       the country code to test.
     * @param expectedLanguages the languages that must be present in the result.
     */
    private static void assertLanguageByCountry(final String countryCode, final String... expectedLanguages) {
        final List<Locale> actualLocales = LocaleUtils.languagesByCountry(countryCode);

        assertNotNull(actualLocales, "The returned list should not be null.");

        // Check that the list is cached
        assertSame(actualLocales, LocaleUtils.languagesByCountry(countryCode),
            "The list should be cached and returned on subsequent calls.");

        // Check that the list is unmodifiable
        assertUnmodifiableCollection(actualLocales);

        // Check that every locale in the list is consistent (correct country, no variant)
        for (final Locale locale : actualLocales) {
            assertEquals(countryCode, locale.getCountry(),
                "Each locale in the list should have the correct country code.");
            assertTrue(StringUtils.isEmpty(locale.getVariant()),
                "Each locale in the list should have an empty variant.");
        }

        // Check that the list contains all expected languages (it may contain more)
        final Set<String> actualLanguageCodes = actualLocales.stream()
            .map(Locale::getLanguage)
            .collect(Collectors.toSet());

        final List<String> expectedLanguageList = Arrays.asList(expectedLanguages);
        assertTrue(actualLanguageCodes.containsAll(expectedLanguageList),
            () -> "List of languages for country '" + countryCode + "' is missing expected languages.\n" +
                  "Expected to contain: " + expectedLanguageList + "\n" +
                  "Actual languages: " + actualLanguageCodes);
    }

    /**
     * Primes the locale cache to prevent potential race conditions during static initialization.
     * This is related to issue LANG-304 and must be run before tests that might access
     * the available locale set.
     */
    @BeforeEach
    void primeLocaleCache() {
        // Testing #LANG-304. Must be called before availableLocaleSet is called.
        LocaleUtils.isAvailableLocale(Locale.getDefault());
    }

    /**
     * Tests that {@link LocaleUtils#toLocale(String)} can correctly parse the string representation
     * of any available locale from the JVM, provided that the locale does not use scripts or extensions.
     * <p>
     * This test iterates through all {@link Locale#getAvailableLocales()} and filters for locales
     * that can be fully represented by the {@code new Locale(language, country, variant)} constructor.
     * This effectively excludes locales with scripts or extensions, which {@code toLocale(String)}
     * is not designed to handle.
     * </p>
     * <p>
     * The test also verifies that if a locale's string representation contains a script/extension
     * marker ('#' or '_#'), {@code toLocale(String)} correctly throws an {@link IllegalArgumentException}.
     * </p>
     *
     * @param availableLocale An available locale from the current JVM.
     */
    @ParameterizedTest
    @MethodSource("java.util.Locale#getAvailableLocales")
    void toLocale_withAvailableLocaleString_recreatesLocale(final Locale availableLocale) {
        // Filter for locales that can be created with the standard 3-argument constructor.
        // This excludes locales with scripts or extensions, which toLocale(String) does not support.
        final Locale simpleLocale = new Locale(availableLocale.getLanguage(), availableLocale.getCountry(), availableLocale.getVariant());
        if (!availableLocale.equals(simpleLocale)) {
            // Skip complex locales with scripts/extensions.
            return;
        }

        final String localeString = availableLocale.toString();

        // Check for script/extension markers which are not supported by toLocale(String).
        int suffixIndex = localeString.indexOf("_#");
        if (suffixIndex == -1) {
            suffixIndex = localeString.indexOf("#");
        }

        String stringToParse = localeString;
        if (suffixIndex != -1) {
            // If a suffix is present, toLocale() should fail for the full string.
            assertIllegalArgumentException(() -> LocaleUtils.toLocale(localeString),
                "toLocale() should throw for strings with script/extension markers.");

            // For the test to proceed, we parse the string without the suffix.
            stringToParse = localeString.substring(0, suffixIndex);
        }

        // Act: Parse the (potentially stripped) string.
        final Locale parsedLocale = LocaleUtils.toLocale(stringToParse);

        // Assert: The parsed locale should be equal to the original available locale.
        assertEquals(availableLocale, parsedLocale);
    }

    /**
     * Tests the behavior of {@link LocaleUtils#toLocale(Locale)}, which should return
     * an equal Locale object for any non-null input.
     *
     * @param availableLocale An available locale from the current JVM.
     */
    @ParameterizedTest
    @MethodSource("java.util.Locale#getAvailableLocales")
    void toLocale_withLocaleAsInput_returnsEqualLocale(final Locale availableLocale) {
        assertEquals(availableLocale, LocaleUtils.toLocale(availableLocale));
    }

    /**
     * Tests the {@link LocaleUtils#languagesByCountry(String)} method with various country codes.
     */
    @Test
    void languagesByCountry_shouldReturnCorrectLanguagesForKnownCountries() {
        assertLanguageByCountry(null);
        assertLanguageByCountry("GB", "en");
        assertLanguageByCountry("ZZ"); // Invalid country code
        assertLanguageByCountry("CH", "fr", "de", "it");
    }
}