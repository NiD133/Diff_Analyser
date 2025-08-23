package org.apache.commons.lang3;

import static org.apache.commons.lang3.JavaVersion.JAVA_1_4;
import static org.apache.commons.lang3.LangAssertions.assertIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class LocaleUtilsTestTest2 extends AbstractLangTest {

    private static final Locale LOCALE_EN = new Locale("en", "");

    private static final Locale LOCALE_EN_US = new Locale("en", "US");

    private static final Locale LOCALE_EN_US_ZZZZ = new Locale("en", "US", "ZZZZ");

    private static final Locale LOCALE_FR = new Locale("fr", "");

    private static final Locale LOCALE_FR_CA = new Locale("fr", "CA");

    private static final Locale LOCALE_QQ = new Locale("qq", "");

    private static final Locale LOCALE_QQ_ZZ = new Locale("qq", "ZZ");

    /**
     * Make sure the country by language is correct. It checks that
     * the LocaleUtils.countryByLanguage(language) call contains the
     * array of countries passed in. It may contain more due to JVM
     * variations.
     *
     * @param language
     * @param countries array of countries that should be returned
     */
    private static void assertCountriesByLanguage(final String language, final String[] countries) {
        final List<Locale> list = LocaleUtils.countriesByLanguage(language);
        final List<Locale> list2 = LocaleUtils.countriesByLanguage(language);
        assertNotNull(list);
        assertSame(list, list2);
        //search through languages
        for (final String country : countries) {
            boolean found = false;
            // see if it was returned by the set
            for (final Locale locale : list) {
                // should have an en empty variant
                assertTrue(StringUtils.isEmpty(locale.getVariant()));
                assertEquals(language, locale.getLanguage());
                if (country.equals(locale.getCountry())) {
                    found = true;
                    break;
                }
            }
            assertTrue(found, "Could not find language: " + country + " for country: " + language);
        }
        assertUnmodifiableCollection(list);
    }

    /**
     * Make sure the language by country is correct. It checks that
     * the LocaleUtils.languagesByCountry(country) call contains the
     * array of languages passed in. It may contain more due to JVM
     * variations.
     *
     * @param country
     * @param languages array of languages that should be returned
     */
    private static void assertLanguageByCountry(final String country, final String[] languages) {
        final List<Locale> list = LocaleUtils.languagesByCountry(country);
        final List<Locale> list2 = LocaleUtils.languagesByCountry(country);
        assertNotNull(list);
        assertSame(list, list2);
        //search through languages
        for (final String language : languages) {
            boolean found = false;
            // see if it was returned by the set
            for (final Locale locale : list) {
                // should have an en empty variant
                assertTrue(StringUtils.isEmpty(locale.getVariant()));
                assertEquals(country, locale.getCountry());
                if (language.equals(locale.getLanguage())) {
                    found = true;
                    break;
                }
            }
            assertTrue(found, "Could not find language: " + language + " for country: " + country);
        }
        assertUnmodifiableCollection(list);
    }

    /**
     * Helper method for local lookups.
     *
     * @param locale  the input locale
     * @param defaultLocale  the input default locale
     * @param expected  expected results
     */
    private static void assertLocaleLookupList(final Locale locale, final Locale defaultLocale, final Locale[] expected) {
        final List<Locale> localeList = defaultLocale == null ? LocaleUtils.localeLookupList(locale) : LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(expected.length, localeList.size());
        assertEquals(Arrays.asList(expected), localeList);
        assertUnmodifiableCollection(localeList);
    }

    /**
     * @param coll  the collection to check
     */
    private static void assertUnmodifiableCollection(final Collection<?> coll) {
        assertThrows(UnsupportedOperationException.class, () -> coll.add(null));
    }

    /**
     * Pass in a valid language, test toLocale.
     *
     * @param language  the language string
     */
    private static void assertValidToLocale(final String language) {
        final Locale locale = LocaleUtils.toLocale(language);
        assertNotNull(locale, "valid locale");
        assertEquals(language, locale.getLanguage());
        //country and variant are empty
        assertTrue(StringUtils.isEmpty(locale.getCountry()));
        assertTrue(StringUtils.isEmpty(locale.getVariant()));
    }

    /**
     * Pass in a valid language, test toLocale.
     *
     * @param localeString to pass to toLocale()
     * @param language of the resulting Locale
     * @param country of the resulting Locale
     */
    private static void assertValidToLocale(final String localeString, final String language, final String country) {
        final Locale locale = LocaleUtils.toLocale(localeString);
        assertNotNull(locale, "valid locale");
        assertEquals(language, locale.getLanguage());
        assertEquals(country, locale.getCountry());
        //variant is empty
        assertTrue(StringUtils.isEmpty(locale.getVariant()));
    }

    /**
     * Pass in a valid language, test toLocale.
     *
     * @param localeString to pass to toLocale()
     * @param language of the resulting Locale
     * @param country of the resulting Locale
     * @param variant of the resulting Locale
     */
    private static void assertValidToLocale(final String localeString, final String language, final String country, final String variant) {
        final Locale locale = LocaleUtils.toLocale(localeString);
        assertNotNull(locale, "valid locale");
        assertEquals(language, locale.getLanguage());
        assertEquals(country, locale.getCountry());
        assertEquals(variant, locale.getVariant());
    }

    @BeforeEach
    public void setUp() {
        // Testing #LANG-304. Must be called before availableLocaleSet is called.
        LocaleUtils.isAvailableLocale(Locale.getDefault());
    }

    @ParameterizedTest
    @MethodSource("java.util.Locale#getAvailableLocales")
    void testParseAllLocales(final Locale actualLocale) {
        // Check if it's possible to recreate the Locale using just the standard constructor
        final Locale locale = new Locale(actualLocale.getLanguage(), actualLocale.getCountry(), actualLocale.getVariant());
        if (actualLocale.equals(locale)) {
            // it is possible for LocaleUtils.toLocale to handle these Locales
            final String str = actualLocale.toString();
            // Look for the script/extension suffix
            int suff = str.indexOf("_#");
            if (suff == -1) {
                suff = str.indexOf("#");
            }
            String localeStr = str;
            if (suff >= 0) {
                // we have a suffix
                assertIllegalArgumentException(() -> LocaleUtils.toLocale(str));
                // try without suffix
                localeStr = str.substring(0, suff);
            }
            final Locale loc = LocaleUtils.toLocale(localeStr);
            assertEquals(actualLocale, loc);
        }
    }

    /**
     * Test toLocale(Locale) method.
     */
    @ParameterizedTest
    @MethodSource("java.util.Locale#getAvailableLocales")
    void testToLocales(final Locale actualLocale) {
        assertEquals(actualLocale, LocaleUtils.toLocale(actualLocale));
    }

    /**
     * Test availableLocaleSet() method.
     */
    @Test
    void testAvailableLocaleSet() {
        final Set<Locale> set = LocaleUtils.availableLocaleSet();
        final Set<Locale> set2 = LocaleUtils.availableLocaleSet();
        assertNotNull(set);
        assertSame(set, set2);
        assertUnmodifiableCollection(set);
        final Locale[] jdkLocaleArray = Locale.getAvailableLocales();
        final List<Locale> jdkLocaleList = Arrays.asList(jdkLocaleArray);
        final Set<Locale> jdkLocaleSet = new HashSet<>(jdkLocaleList);
        assertEquals(jdkLocaleSet, set);
    }
}
