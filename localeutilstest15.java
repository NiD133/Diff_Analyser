package org.apache.commons.lang3;

import static org.apache.commons.lang3.LangAssertions.assertIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link LocaleUtils}.
 */
public class LocaleUtilsTest extends AbstractLangTest {

    /**
     * Helper method to assert that a locale string is parsed correctly.
     *
     * @param localeString the string to parse
     * @param language     the expected language code
     * @param country      the expected country code
     */
    private static void assertValidToLocale(final String localeString, final String language, final String country) {
        final Locale locale = LocaleUtils.toLocale(localeString);
        assertNotNull(locale, "valid locale");
        assertEquals(language, locale.getLanguage());
        assertEquals(country, locale.getCountry());
        // variant is expected to be empty for this helper
        assertTrue(StringUtils.isEmpty(locale.getVariant()));
    }

    /**
     * Finds the starting index of a BCP 47 script or extension in a locale string.
     * For example, in "th_TH_#u-nu-thai", it would return the index of '#'.
     *
     * @param localeString The locale string to check.
     * @return The index of the first extension separator, or -1 if not found.
     */
    private static int findFirstExtensionIndex(final String localeString) {
        final int underscoreSharpIndex = localeString.indexOf("_#");
        if (underscoreSharpIndex != -1) {
            return underscoreSharpIndex;
        }
        return localeString.indexOf('#');
    }

    @BeforeEach
    public void setUp() {
        // Testing #LANG-304. This must be called before availableLocaleSet is initialized
        // to ensure the test environment is consistent.
        LocaleUtils.isAvailableLocale(Locale.getDefault());
    }

    /**
     * Tests that {@link LocaleUtils#toLocale(Locale)} returns a Locale equal to the input.
     */
    @ParameterizedTest
    @MethodSource("java.util.Locale#getAvailableLocales")
    void testToLocale_withLocaleArgument_returnsEqualLocale(final Locale locale) {
        assertEquals(locale, LocaleUtils.toLocale(locale));
    }

    /**
     * Tests that {@link LocaleUtils#toLocale(String)} can correctly parse all available JVM locales,
     * including those with extensions that it is expected to reject.
     */
    @ParameterizedTest
    @MethodSource("java.util.Locale#getAvailableLocales")
    void testToLocale_handlesAllAvailableJvmLocales(final Locale availableLocale) {
        // This test is only valid for locales that can be represented by the
        // (language, country, variant) constructor. Locales with scripts or
        // extensions will not be equal after simple reconstruction and are skipped.
        final Locale simpleReconstruction = new Locale(availableLocale.getLanguage(), availableLocale.getCountry(), availableLocale.getVariant());
        if (!availableLocale.equals(simpleReconstruction)) {
            return; // Skip locales that can't be recreated with the simple constructor.
        }

        final String originalLocaleString = availableLocale.toString();
        String stringToParse = originalLocaleString;

        // BCP 47 extensions (e.g., "_#u-co-phonebk" or "#script") are not supported by toLocale.
        // We first check if such an extension exists.
        final int extensionIndex = findFirstExtensionIndex(originalLocaleString);

        if (extensionIndex != -1) {
            // If an extension exists, verify that toLocale fails for the full string.
            assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(originalLocaleString),
                "Should throw for locale string with extensions: " + originalLocaleString);

            // For the subsequent check, we use the string without the extension.
            stringToParse = originalLocaleString.substring(0, extensionIndex);
        }

        // Verify that the (potentially stripped) string can be parsed back to the original locale.
        final Locale parsedLocale = LocaleUtils.toLocale(stringToParse);
        assertEquals(availableLocale, parsedLocale);
    }

    /**
     * Tests {@link LocaleUtils#toLocale(String)} with two-part locale strings,
     * verifying both valid and invalid formats.
     */
    @Test
    void testToLocale_withTwoPartString_validatesFormat() {
        // Test valid formats
        assertValidToLocale("us_EN", "us", "EN");
        assertValidToLocale("us-EN", "us", "EN");
        assertValidToLocale("us_ZH", "us", "ZH"); // valid format, though locale may not exist

        // Test invalid formats
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("us_En"), "Country code must be uppercase");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("us_en"), "Country code must be uppercase");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("us_eN"), "Country code must be uppercase");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("uS_EN"), "Language code must be lowercase");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("us_E3"), "Country code must be letters");
    }
}