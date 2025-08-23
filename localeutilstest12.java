package org.apache.commons.lang3;

import static org.apache.commons.lang3.LangAssertions.assertIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link LocaleUtils}.
 */
public class LocaleUtilsTest extends AbstractLangTest {

    private static final Locale LOCALE_EN = new Locale("en", "");
    private static final Locale LOCALE_EN_US = new Locale("en", "US");
    private static final Locale LOCALE_EN_US_ZZZZ = new Locale("en", "US", "ZZZZ");
    private static final Locale LOCALE_FR = new Locale("fr", "");
    private static final Locale LOCALE_FR_CA = new Locale("fr", "CA");
    private static final Locale LOCALE_QQ = new Locale("qq", "");
    private static final Locale LOCALE_QQ_ZZ = new Locale("qq", "ZZ");

    @BeforeEach
    public void setUp() {
        // Testing #LANG-304.
        // This must be called before availableLocaleSet is accessed to ensure the test runs consistently.
        LocaleUtils.isAvailableLocale(Locale.getDefault());
    }

    @ParameterizedTest
    @MethodSource("java.util.Locale#getAvailableLocales")
    @DisplayName("toLocale(Locale) should return an equivalent locale")
    void toLocaleWithLocaleArgumentShouldReturnEquivalentLocale(final Locale locale) {
        assertEquals(locale, LocaleUtils.toLocale(locale));
    }

    @ParameterizedTest
    @MethodSource("java.util.Locale#getAvailableLocales")
    @DisplayName("toLocale(String) should correctly parse strings from available locales")
    void toLocaleShouldParseStringFromAvailableLocale(final Locale availableLocale) {
        // Skips locales with scripts or extensions, as they cannot be fully represented
        // by the Locale(lang, country, variant) constructor and are not supported by toLocale(String).
        final Locale simpleLocale = new Locale(availableLocale.getLanguage(), availableLocale.getCountry(), availableLocale.getVariant());
        if (!availableLocale.equals(simpleLocale)) {
            return;
        }

        final String localeString = availableLocale.toString();

        // The string representation of a locale might include an extension (e.g., "en_US_#Latn").
        // LocaleUtils.toLocale does not support these extensions.
        int extensionIndex = localeString.indexOf("_#");
        if (extensionIndex == -1) {
            extensionIndex = localeString.indexOf('#');
        }

        String stringToParse = localeString;
        if (extensionIndex != -1) {
            // Verify that toLocale throws for the string with an extension.
            assertIllegalArgumentException(() -> LocaleUtils.toLocale(localeString));
            // For the rest of the test, use the string without the extension.
            stringToParse = localeString.substring(0, extensionIndex);
        }

        // Verify that the (potentially stripped) string is parsed back to the original locale.
        final Locale parsedLocale = LocaleUtils.toLocale(stringToParse);
        assertEquals(availableLocale, parsedLocale);
    }

    static Stream<Arguments> localeLookupListWithDefaultLocaleProvider() {
        return Stream.of(
            arguments(LOCALE_QQ, LOCALE_QQ, List.of(LOCALE_QQ)),
            arguments(LOCALE_EN, LOCALE_EN, List.of(LOCALE_EN)),
            arguments(LOCALE_EN_US, LOCALE_EN_US, List.of(LOCALE_EN_US, LOCALE_EN)),
            arguments(LOCALE_EN_US, LOCALE_QQ, List.of(LOCALE_EN_US, LOCALE_EN, LOCALE_QQ)),
            arguments(LOCALE_EN_US, LOCALE_QQ_ZZ, List.of(LOCALE_EN_US, LOCALE_EN, LOCALE_QQ_ZZ)),
            arguments(LOCALE_EN_US_ZZZZ, null, List.of(LOCALE_EN_US_ZZZZ, LOCALE_EN_US, LOCALE_EN)),
            arguments(LOCALE_EN_US_ZZZZ, LOCALE_EN_US_ZZZZ, List.of(LOCALE_EN_US_ZZZZ, LOCALE_EN_US, LOCALE_EN)),
            arguments(LOCALE_EN_US_ZZZZ, LOCALE_QQ, List.of(LOCALE_EN_US_ZZZZ, LOCALE_EN_US, LOCALE_EN, LOCALE_QQ)),
            arguments(LOCALE_EN_US_ZZZZ, LOCALE_QQ_ZZ, List.of(LOCALE_EN_US_ZZZZ, LOCALE_EN_US, LOCALE_EN, LOCALE_QQ_ZZ)),
            arguments(LOCALE_FR_CA, LOCALE_EN, List.of(LOCALE_FR_CA, LOCALE_FR, LOCALE_EN))
        );
    }

    @ParameterizedTest
    @MethodSource("localeLookupListWithDefaultLocaleProvider")
    @DisplayName("localeLookupList should return the correct hierarchy of locales")
    void testLocaleLookupListWithDefaultLocale(final Locale locale, final Locale defaultLocale, final List<Locale> expected) {
        final List<Locale> actual = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(expected, actual);
        assertUnmodifiableCollection(actual);
    }

    /**
     * Asserts that the given collection is unmodifiable.
     *
     * @param coll the collection to check.
     */
    private static void assertUnmodifiableCollection(final Collection<?> coll) {
        assertThrows(UnsupportedOperationException.class, () -> coll.add(null));
    }
}