package org.apache.commons.lang3;

import static org.apache.commons.lang3.LangAssertions.assertIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link LocaleUtils#toLocale(String)} and {@link LocaleUtils#toLocale(Locale)} methods.
 *
 * <p>The setUp method addresses a specific initialization issue (LANG-304)
 * by ensuring the available locale set is initialized before tests run.</p>
 */
@DisplayName("LocaleUtils.toLocale Tests")
class LocaleUtilsToLocaleTest extends AbstractLangTest {

    @BeforeEach
    void setUp() {
        // This call is required due to a bug (LANG-304) where the available locale set
        // might not be initialized correctly. Calling this ensures it's ready for the tests.
        LocaleUtils.isAvailableLocale(Locale.getDefault());
    }

    @DisplayName("should correctly parse string representations of all available system locales")
    @ParameterizedTest(name = "Locale: {0}")
    @MethodSource("java.util.Locale#getAvailableLocales")
    void toLocaleFromString_shouldParseAvailableLocales(final Locale availableLocale) {
        // Precondition: Test only locales that can be recreated with the standard constructor.
        // Some locales from getAvailableLocales() might have properties (like script or extensions)
        // that the new Locale(lang, country, variant) constructor doesn't preserve.
        final Locale reconstructableLocale = new Locale(availableLocale.getLanguage(), availableLocale.getCountry(), availableLocale.getVariant());
        if (!availableLocale.equals(reconstructableLocale)) {
            // Skip locales that cannot be faithfully reconstructed, as toLocale cannot be expected to handle them.
            return;
        }

        final String localeStr = availableLocale.toString();

        // The SUT (LocaleUtils.toLocale) does not support locale strings with script/extension
        // suffixes (e.g., "sr_RS_#Latn"). These are identified by "_#" or "#".
        int suffixIndex = localeStr.indexOf("_#");
        if (suffixIndex == -1) {
            suffixIndex = localeStr.indexOf("#");
        }

        if (suffixIndex >= 0) {
            // The string contains a suffix, which is not supported.
            // Verify that toLocale throws an exception for the full string.
            assertIllegalArgumentException(() -> LocaleUtils.toLocale(localeStr),
                "toLocale should fail for locale string with suffix: " + localeStr);

            // Test again with the suffix removed to ensure the base locale is parsed correctly.
            final String baseLocaleStr = localeStr.substring(0, suffixIndex);
            final Locale parsedLocale = LocaleUtils.toLocale(baseLocaleStr);
            assertEquals(availableLocale, parsedLocale);
        } else {
            // For standard locale strings without suffixes, parse and verify equality.
            final Locale parsedLocale = LocaleUtils.toLocale(localeStr);
            assertEquals(availableLocale, parsedLocale);
        }
    }

    @DisplayName("should return the same locale when given a Locale object")
    @ParameterizedTest(name = "Locale: {0}")
    @MethodSource("java.util.Locale#getAvailableLocales")
    void toLocaleFromLocale_shouldReturnSameLocale(final Locale availableLocale) {
        assertEquals(availableLocale, LocaleUtils.toLocale(availableLocale));
    }

    @Test
    @DisplayName("should return null when given a null String")
    void toLocaleFromString_shouldReturnNullForNullInput() {
        assertNull(LocaleUtils.toLocale((String) null));
    }

    @Test
    @DisplayName("should return the default locale when given a null Locale")
    void toLocaleFromLocale_shouldReturnDefaultLocaleForNullInput() {
        assertEquals(Locale.getDefault(), LocaleUtils.toLocale((Locale) null));
    }

    @Test
    @DisplayName("should return the default locale when given the default Locale")
    void toLocaleFromLocale_shouldReturnDefaultLocaleForDefaultLocaleInput() {
        // This test is somewhat redundant with the parameterized test above,
        // but it makes the behavior for the default locale explicit.
        assertEquals(Locale.getDefault(), LocaleUtils.toLocale(Locale.getDefault()));
    }
}