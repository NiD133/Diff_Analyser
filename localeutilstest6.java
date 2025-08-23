package org.apache.commons.lang3;

import static org.apache.commons.lang3.LangAssertions.assertIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Unit tests for {@link LocaleUtils}.
 */
public class LocaleUtilsTest {

    @BeforeEach
    void setUp() {
        // Testing #LANG-304. Must be called before availableLocaleSet is called.
        LocaleUtils.isAvailableLocale(Locale.getDefault());
    }

    /**
     * Tests {@link LocaleUtils#toLocale(String)} with all locales available on the system.
     */
    @ParameterizedTest
    @MethodSource("java.util.Locale#getAvailableLocales")
    @DisplayName("toLocale(String) should correctly parse all available locale strings")
    void toLocale_shouldHandleAllAvailableLocales(final Locale availableLocale) {
        // The toLocale(String) method only supports language, country, and variant.
        // We create an expected locale that strips any script or extension data.
        final Locale expectedLocale = new Locale(availableLocale.getLanguage(), availableLocale.getCountry(), availableLocale.getVariant());
        final String localeString = availableLocale.toString();

        // The toLocale(String) method does not support BCP 47 script or extension sections,
        // which are indicated by "_#" or "#".
        int suffixIndex = localeString.indexOf("_#");
        if (suffixIndex == -1) {
            suffixIndex = localeString.indexOf('#');
        }

        if (suffixIndex >= 0) {
            // Strings with suffixes are expected to fail parsing.
            assertIllegalArgumentException(() -> LocaleUtils.toLocale(localeString),
                "Parsing locale string with suffix should fail: " + localeString);

            // Test parsing the string without the unsupported suffix.
            final String baseLocaleString = localeString.substring(0, suffixIndex);
            final Locale parsedLocale = LocaleUtils.toLocale(baseLocaleString);
            assertEquals(expectedLocale, parsedLocale);
        } else {
            // For locales without suffixes, parsing should work directly.
            assertEquals(expectedLocale, LocaleUtils.toLocale(localeString));
        }
    }

    /**
     * Tests that {@link LocaleUtils#toLocale(Locale)} returns the same locale instance.
     */
    @ParameterizedTest
    @MethodSource("java.util.Locale#getAvailableLocales")
    @DisplayName("toLocale(Locale) should return the input locale instance")
    void toLocale_withLocale_shouldReturnInput(final Locale actualLocale) {
        assertEquals(actualLocale, LocaleUtils.toLocale(actualLocale));
    }

    @Nested
    @DisplayName("isLanguageUndetermined(Locale) tests")
    class IsLanguageUndeterminedTests {

        @Test
        @DisplayName("should return true for a null locale")
        void shouldReturnTrueForNullLocale() {
            assertTrue(LocaleUtils.isLanguageUndetermined(null));
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "a", "12", "und"})
        @DisplayName("should return true for locales with an undetermined language tag")
        void shouldReturnTrueForLocalesWithUndeterminedLanguageTag(final String language) {
            // Locales with empty or malformed language codes result in the "und" (undetermined) language tag.
            final Locale localeWithUndeterminedLanguage = new Locale(language);
            assertTrue(LocaleUtils.isLanguageUndetermined(localeWithUndeterminedLanguage),
                "Locale with language '" + language + "' should be undetermined.");
        }

        static Stream<Locale> determinedLocales() {
            return Stream.of(
                new Locale("en"),
                new Locale("en", "US"),
                new Locale("en", "US", "ZZZZ"),
                new Locale("fr"),
                new Locale("fr", "CA"),
                // "qq" is a valid 2-letter code format, so it is not undetermined.
                new Locale("qq"),
                new Locale("qq", "ZZ")
            );
        }

        @ParameterizedTest
        @MethodSource("determinedLocales")
        @DisplayName("should return false for locales with a determined language tag")
        void shouldReturnFalseForLocalesWithDeterminedLanguageTag(final Locale locale) {
            assertFalse(LocaleUtils.isLanguageUndetermined(locale),
                "Locale '" + locale + "' should be determined.");
        }
    }
}