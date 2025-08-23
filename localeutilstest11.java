package org.apache.commons.lang3;

import static org.apache.commons.lang3.LangAssertions.assertIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Unit tests for {@link LocaleUtils}, focusing on locale parsing and lookup list generation.
 */
public class LocaleUtilsTest extends AbstractLangTest {

    private static final Locale LOCALE_EN = new Locale("en", "");
    private static final Locale LOCALE_EN_US = new Locale("en", "US");
    private static final Locale LOCALE_EN_US_ZZZZ = new Locale("en", "US", "ZZZZ");
    private static final Locale LOCALE_QQ = new Locale("qq", "");

    @BeforeEach
    public void setUp() {
        // Testing #LANG-304. Must be called before availableLocaleSet is called to avoid a bug.
        LocaleUtils.isAvailableLocale(Locale.getDefault());
    }

    @DisplayName("toLocale(String) should correctly parse all available locale strings")
    @ParameterizedTest
    @MethodSource("java.util.Locale#getAvailableLocales")
    void toLocaleFromString_shouldCorrectlyParseAllAvailableLocales(final Locale actualLocale) {
        // Skips locales with scripts or extensions that cannot be represented by the basic
        // new Locale(lang, country, variant) constructor, as toLocale(String) does not support them.
        final Locale reconstructedLocale = new Locale(actualLocale.getLanguage(), actualLocale.getCountry(), actualLocale.getVariant());
        if (!actualLocale.equals(reconstructedLocale)) {
            return;
        }

        String stringToParse = actualLocale.toString();

        // Locale.toString() can produce strings with script/extension suffixes (e.g., "_#Latn")
        // which toLocale(String) is not designed to handle.
        int extensionSeparatorIndex = stringToParse.indexOf("_#");
        if (extensionSeparatorIndex == -1) {
            extensionSeparatorIndex = stringToParse.indexOf("#");
        }

        if (extensionSeparatorIndex >= 0) {
            // Verify that toLocale fails for the full string containing an extension.
            final String fullString = stringToParse;
            assertIllegalArgumentException(() -> LocaleUtils.toLocale(fullString),
                "Expected to fail for locale string with extension: " + fullString);
            // For the rest of the test, use the string without the extension.
            stringToParse = stringToParse.substring(0, extensionSeparatorIndex);
        }

        // Verify that the (potentially truncated) string is parsed back to the original locale.
        // This works because the initial guard has already filtered out locales where information
        // (like a script) would be lost by this truncation.
        final Locale parsedLocale = LocaleUtils.toLocale(stringToParse);
        assertEquals(actualLocale, parsedLocale);
    }

    @DisplayName("toLocale(Locale) should return the same locale instance")
    @ParameterizedTest
    @MethodSource("java.util.Locale#getAvailableLocales")
    void toLocaleFromLocale_shouldReturnSameInstance(final Locale actualLocale) {
        assertSame(actualLocale, LocaleUtils.toLocale(actualLocale));
    }

    @Test
    @DisplayName("localeLookupList(Locale) should return the correct hierarchy of locales")
    void localeLookupList_shouldReturnLocaleHierarchy() {
        assertLocaleLookupList(null, null); // Expect empty list
        assertLocaleLookupList(LOCALE_QQ, null, LOCALE_QQ);
        assertLocaleLookupList(LOCALE_EN, null, LOCALE_EN);
        assertLocaleLookupList(LOCALE_EN_US, null, LOCALE_EN_US, LOCALE_EN);
        assertLocaleLookupList(LOCALE_EN_US_ZZZZ, null, LOCALE_EN_US_ZZZZ, LOCALE_EN_US, LOCALE_EN);
    }

    /**
     * Asserts that the locale lookup list is as expected and is unmodifiable.
     *
     * @param locale        The locale to look up.
     * @param defaultLocale The default locale.
     * @param expected      The expected list of locales.
     */
    private static void assertLocaleLookupList(final Locale locale, final Locale defaultLocale, final Locale... expected) {
        final List<Locale> localeList = defaultLocale == null
            ? LocaleUtils.localeLookupList(locale)
            : LocaleUtils.localeLookupList(locale, defaultLocale);

        assertEquals(Arrays.asList(expected), localeList);
        assertUnmodifiableCollection(localeList);
    }

    /**
     * Asserts that the given collection is unmodifiable.
     *
     * @param coll The collection to check.
     */
    private static void assertUnmodifiableCollection(final Collection<?> coll) {
        assertThrows(UnsupportedOperationException.class, () -> coll.add(null));
    }
}