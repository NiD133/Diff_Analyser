package org.apache.commons.lang3;

import static org.apache.commons.lang3.LangAssertions.assertIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link LocaleUtils}, focusing on parsing locale strings.
 */
public class LocaleUtilsStringToLocaleTest extends AbstractLangTest {

    /**
     * A helper assertion to verify that a locale string is correctly parsed into a Locale object
     * with a language, country, and variant.
     *
     * @param localeString the string to parse.
     * @param language     the expected language code.
     * @param country      the expected country code.
     * @param variant      the expected variant.
     */
    private static void assertValidToLocale(final String localeString, final String language, final String country, final String variant) {
        final Locale locale = LocaleUtils.toLocale(localeString);
        assertNotNull(locale, "valid locale");
        assertEquals(language, locale.getLanguage());
        assertEquals(country, locale.getCountry());
        assertEquals(variant, locale.getVariant());
    }

    /**
     * A helper assertion to verify that a locale string is correctly parsed into a Locale object
     * with a language and country.
     *
     * @param localeString the string to pass to toLocale().
     * @param language     the expected language of the resulting Locale.
     * @param country      the expected country of the resulting Locale.
     */
    private static void assertValidToLocale(final String localeString, final String language, final String country) {
        final Locale locale = LocaleUtils.toLocale(localeString);
        assertNotNull(locale, "valid locale");
        assertEquals(language, locale.getLanguage());
        assertEquals(country, locale.getCountry());
        // The variant should be empty.
        assertTrue(StringUtils.isEmpty(locale.getVariant()));
    }

    @BeforeEach
    public void setUp() {
        // Testing #LANG-304. This must be called before availableLocaleSet is used for the first time
        // by any of the tests, to ensure the cache is initialized correctly.
        LocaleUtils.isAvailableLocale(Locale.getDefault());
    }

    @ParameterizedTest
    @MethodSource("java.util.Locale#getAvailableLocales")
    @DisplayName("toLocale(String) should correctly parse string representations of available locales")
    void testToLocale_parsesStringFromAvailableLocale(final Locale locale) {
        // toLocale is not designed to handle locales with scripts or extensions,
        // which are indicated by '#' in the string representation. We skip these.
        if (locale.toString().contains("#")) {
            return;
        }

        // The original test included a filter to only test locales that can be recreated
        // with the new Locale(lang, country, variant) constructor. This effectively
        // filters out locales with modern extensions, which toLocale() doesn't support.
        final Locale simpleLocale = new Locale(locale.getLanguage(), locale.getCountry(), locale.getVariant());
        if (!locale.equals(simpleLocale)) {
            return;
        }

        // Test that toLocale can round-trip the string representation.
        final String localeString = locale.toString();
        final Locale parsedLocale = LocaleUtils.toLocale(localeString);
        assertEquals(locale, parsedLocale);
    }

    @Test
    @DisplayName("toLocale(String) should throw an exception for strings with script/extension markers")
    void testToLocale_rejectsStringsWithScriptOrExtensionMarkers() {
        // These formats are not supported by toLocale(String) and should be rejected.
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("en_US_#u-va-posix"));
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("th_TH_#u-nu-thai"));
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("en__#foo"));
    }

    @ParameterizedTest
    @MethodSource("java.util.Locale#getAvailableLocales")
    @DisplayName("toLocale(Locale) should return the same locale instance")
    void testToLocale_withLocaleArgument_returnsSameLocale(final Locale actualLocale) {
        assertEquals(actualLocale, LocaleUtils.toLocale(actualLocale));
    }

    @Test
    @DisplayName("Tests LANG-865: toLocale() with strings starting with an underscore")
    void testToLocale_forStringsStartingWithUnderscore() {
        // Test valid cases for strings starting with an underscore.
        assertValidToLocale("_GB", "", "GB", "");
        assertValidToLocale("_GB_P", "", "GB", "P");
        assertValidToLocale("_GB_POSIX", "", "GB", "POSIX");

        // Test invalid cases that should throw an exception.
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("_G"), "Must be at least 3 chars if starts with underscore");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("_Gb"), "Country code must be uppercase");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("_gB"), "Country code must be uppercase");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("_1B"), "Country code must be letters");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("_G1"), "Country code must be letters");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("_GB_"), "Must be at least 5 chars if variant is present");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("_GBAP"), "Must have underscore after country if variant is present");
    }
}