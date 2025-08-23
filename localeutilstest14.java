package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link LocaleUtils}.
 * This class focuses on the toLocale() methods.
 */
public class LocaleUtilsTest extends AbstractLangTest {

    @BeforeEach
    public void setUp() {
        // Testing #LANG-304. This must be called before availableLocaleSet is initialized.
        LocaleUtils.isAvailableLocale(Locale.getDefault());
    }

    /**
     * Tests that {@link LocaleUtils#toLocale(String)} returns null for a null input string.
     */
    @Test
    void toLocale_withNullString_shouldReturnNull() {
        assertNull(LocaleUtils.toLocale((String) null));
    }

    /**
     * Tests that {@link LocaleUtils#toLocale(String)} correctly handles an empty string,
     * returning an empty Locale. This is relevant for modern JDKs where the empty
     * locale is a default.
     */
    @Test
    void toLocale_withEmptyString_shouldReturnEmptyLocale() {
        final Locale expected = new Locale("", "");
        assertEquals(expected, LocaleUtils.toLocale(""));
    }

    /**
     * Tests that {@link LocaleUtils#toLocale(String)} correctly creates Locales for
     * valid two-letter language codes.
     */
    @Test
    void toLocale_withTwoLetterLanguageCode_shouldCreateLocale() {
        assertEquals(new Locale("us"), LocaleUtils.toLocale("us"));
        assertEquals(new Locale("fr"), LocaleUtils.toLocale("fr"));
        assertEquals(new Locale("de"), LocaleUtils.toLocale("de"));
        assertEquals(new Locale("zh"), LocaleUtils.toLocale("zh"));
    }

    /**
     * Tests that {@link LocaleUtils#toLocale(String)} creates a Locale instance even for
     * a language code that is syntactically valid but does not correspond to a known language.
     */
    @Test
    void toLocale_withNonExistentButValidLanguageCode_shouldCreateLocale() {
        final Locale expected = new Locale("qq");
        assertEquals(expected, LocaleUtils.toLocale("qq"));
    }

    /**
     * Tests that {@link LocaleUtils#toLocale(String)} throws an IllegalArgumentException
     * for various malformed locale strings.
     *
     * @param invalidLocaleString A malformed locale string.
     */
    @ParameterizedTest(name = "toLocale(\"{0}\") should throw IAE")
    @ValueSource(strings = {
        "Us",   // Language part must be lowercase
        "uS",   // Language part must be lowercase
        "u#",   // Contains invalid characters
        "u",    // Invalid length (must be 2, 5, or >= 7)
        "uu_U"  // Invalid length (must be 2, 5, or >= 7)
    })
    void toLocale_withInvalidFormat_shouldThrowIllegalArgumentException(final String invalidLocaleString) {
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(invalidLocaleString));
    }

    /**
     * Tests that {@link LocaleUtils#toLocale(Locale)} returns the same locale instance that was passed in.
     *
     * @param locale A locale from the set of available system locales.
     */
    @ParameterizedTest
    @MethodSource("java.util.Locale#getAvailableLocales")
    void toLocale_withLocale_shouldReturnSameLocale(final Locale locale) {
        assertEquals(locale, LocaleUtils.toLocale(locale));
    }

    /**
     * Tests that {@link LocaleUtils#toLocale(String)} can correctly parse the string representation
     * of all available locales on the system.
     * <p>
     * Some locales have scripts or extensions (e.g., "sr_RS_#Latn" or "th_TH_#u-nu-thai")
     * which are not supported by {@code toLocale(String)}. This test verifies that such strings
     * cause an exception, but that the base locale (without the suffix) can be parsed correctly.
     * </p>
     *
     * @param systemLocale A locale from the set of available system locales.
     */
    @ParameterizedTest
    @MethodSource("java.util.Locale#getAvailableLocales")
    void toLocale_shouldCorrectlyParseStringRepresentationOfAvailableLocales(final Locale systemLocale) {
        final String localeString = systemLocale.toString();

        // Locales with extensions/scripts produce a toString() with a '#' suffix.
        final int suffixIndex = localeString.indexOf('#');

        if (suffixIndex != -1) {
            // toLocale does not support the # suffix and should throw an exception.
            assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(localeString),
                () -> "Should throw for locale string with suffix: " + localeString);

            // However, it should parse the part before the suffix.
            final String baseLocaleString = localeString.substring(0, suffixIndex);
            final Locale expectedBaseLocale = new Locale(systemLocale.getLanguage(), systemLocale.getCountry(), systemLocale.getVariant());
            assertEquals(expectedBaseLocale, LocaleUtils.toLocale(baseLocaleString));
        } else {
            // For locales without suffixes, toLocale should parse them correctly,
            // provided they don't contain information beyond language, country, and variant.
            final Locale simpleLocale = new Locale(systemLocale.getLanguage(), systemLocale.getCountry(), systemLocale.getVariant());
            if (systemLocale.equals(simpleLocale)) {
                assertEquals(systemLocale, LocaleUtils.toLocale(localeString));
            }
            // If not equal, systemLocale has information (like extensions) that toLocale
            // cannot reconstruct. We skip these as it's outside toLocale's documented scope.
        }
    }
}