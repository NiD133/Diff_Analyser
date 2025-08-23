package org.apache.commons.lang3;

import static org.apache.commons.lang3.LangAssertions.assertIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link LocaleUtils} focusing on locale availability and parsing from strings.
 */
public class LocaleUtilsAvailabilityAndParsingTest extends AbstractLangTest {

    private static final Locale LOCALE_EN = new Locale("en", "");
    private static final Locale LOCALE_EN_US = new Locale("en", "US");
    private static final Locale LOCALE_EN_US_ZZZZ = new Locale("en", "US", "ZZZZ");
    private static final Locale LOCALE_FR = new Locale("fr", "");
    private static final Locale LOCALE_FR_CA = new Locale("fr", "CA");
    private static final Locale LOCALE_QQ = new Locale("qq", ""); // Fictional language
    private static final Locale LOCALE_QQ_ZZ = new Locale("qq", "ZZ"); // Fictional country

    @BeforeEach
    void setUp() {
        // Primes the cache for LocaleUtils. This is to test a specific issue (LANG-304)
        // where the cache needs to be initialized before other methods are called.
        LocaleUtils.isAvailableLocale(Locale.getDefault());
    }

    @Test
    @DisplayName("isAvailableLocale() should be consistent with availableLocaleSet()")
    void isAvailableLocale_shouldBeConsistentWithAvailableLocaleSet() {
        final Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();

        // The result of isAvailableLocale(locale) should be the same as availableLocaleSet().contains(locale)
        assertEquals(availableLocales.contains(LOCALE_EN), LocaleUtils.isAvailableLocale(LOCALE_EN));
        assertEquals(availableLocales.contains(LOCALE_EN_US), LocaleUtils.isAvailableLocale(LOCALE_EN_US));
        assertEquals(availableLocales.contains(LOCALE_EN_US_ZZZZ), LocaleUtils.isAvailableLocale(LOCALE_EN_US_ZZZZ));
        assertEquals(availableLocales.contains(LOCALE_FR), LocaleUtils.isAvailableLocale(LOCALE_FR));
        assertEquals(availableLocales.contains(LOCALE_FR_CA), LocaleUtils.isAvailableLocale(LOCALE_FR_CA));
        assertEquals(availableLocales.contains(LOCALE_QQ), LocaleUtils.isAvailableLocale(LOCALE_QQ));
        assertEquals(availableLocales.contains(LOCALE_QQ_ZZ), LocaleUtils.isAvailableLocale(LOCALE_QQ_ZZ));
    }

    @ParameterizedTest
    @MethodSource("java.util.Locale#getAvailableLocales")
    @DisplayName("toLocale(Locale) should return an equal locale for any non-null available locale")
    void toLocale_withLocaleArgument_returnsEqualLocale(final Locale availableLocale) {
        assertEquals(availableLocale, LocaleUtils.toLocale(availableLocale));
    }

    @ParameterizedTest
    @MethodSource("java.util.Locale#getAvailableLocales")
    @DisplayName("toLocale(String) should correctly parse string representations of available locales")
    void toLocale_parsesStringsOfAvailableLocales(final Locale availableLocale) {
        // Some locales, especially those with scripts or extensions, cannot be reconstructed
        // with the new Locale(lang, country, variant) constructor. We test only those that can be.
        final Locale reconstructedLocale = new Locale(availableLocale.getLanguage(), availableLocale.getCountry(), availableLocale.getVariant());
        if (!availableLocale.equals(reconstructedLocale)) {
            return; // Skip locales that can't be recreated with the simple constructor.
        }

        final String localeString = availableLocale.toString();

        // LocaleUtils.toLocale does not support modern extensions (e.g., "_#Latn" or "#WINDOWS").
        // We need to detect if the locale string contains such an extension.
        int suffixPosition = localeString.indexOf("_#");
        if (suffixPosition == -1) {
            suffixPosition = localeString.indexOf('#');
        }

        String stringToParse = localeString;
        if (suffixPosition >= 0) {
            // If an extension exists, toLocale should fail when parsing the full string.
            assertIllegalArgumentException(() -> LocaleUtils.toLocale(localeString),
                "Expected toLocale to fail for string with extension: " + localeString);

            // For the positive test case, we strip the unsupported extension.
            stringToParse = localeString.substring(0, suffixPosition);
        }

        // toLocale should correctly parse the string (with the extension stripped, if it existed).
        final Locale parsedLocale = LocaleUtils.toLocale(stringToParse);
        assertEquals(availableLocale, parsedLocale);
    }
}