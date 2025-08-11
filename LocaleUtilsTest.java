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

/**
 * Tests for {@link LocaleUtils}.
 * 
 * This suite focuses on:
 * - Making expectations explicit.
 * - Removing duplication by centralizing common assertions.
 * - Adding small comments where behavior is non-obvious.
 */
class LocaleUtilsTest extends AbstractLangTest {

    private static final Locale LOCALE_EN = new Locale("en", "");
    private static final Locale LOCALE_EN_US = new Locale("en", "US");
    private static final Locale LOCALE_EN_US_ZZZZ = new Locale("en", "US", "ZZZZ");
    private static final Locale LOCALE_FR = new Locale("fr", "");
    private static final Locale LOCALE_FR_CA = new Locale("fr", "CA");
    private static final Locale LOCALE_QQ = new Locale("qq", "");
    private static final Locale LOCALE_QQ_ZZ = new Locale("qq", "ZZ");

    /**
     * Asserts that LocaleUtils.countriesByLanguage(language) returns an unmodifiable list which:
     * - is cached (same instance on repeated calls), and
     * - contains locales with the given language and the expected set of countries (allowing JVM-specific supersets).
     */
    private static void assertCountriesByLanguageContains(final String language, final String... expectedCountries) {
        final List<Locale> localesOnce = LocaleUtils.countriesByLanguage(language);
        final List<Locale> localesAgain = LocaleUtils.countriesByLanguage(language);

        assertNotNull(localesOnce);
        assertSame(localesOnce, localesAgain, "countriesByLanguage should be cached and return the same instance");
        assertUnmodifiableCollection(localesOnce);

        for (final String expectedCountry : expectedCountries) {
            boolean found = false;
            for (final Locale locale : localesOnce) {
                assertTrue(StringUtils.isEmpty(locale.getVariant()), "Expected empty variant");
                assertEquals(language, locale.getLanguage(), "Language mismatch in returned locale");
                if (expectedCountry.equals(locale.getCountry())) {
                    found = true;
                    break;
                }
            }
            assertTrue(found, "Missing expected country '" + expectedCountry + "' for language '" + language + "'");
        }
    }

    /**
     * Asserts that LocaleUtils.languagesByCountry(country) returns an unmodifiable list which:
     * - is cached (same instance on repeated calls), and
     * - contains locales with the given country and the expected set of languages (allowing JVM-specific supersets).
     */
    private static void assertLanguagesByCountryContains(final String country, final String... expectedLanguages) {
        final List<Locale> localesOnce = LocaleUtils.languagesByCountry(country);
        final List<Locale> localesAgain = LocaleUtils.languagesByCountry(country);

        assertNotNull(localesOnce);
        assertSame(localesOnce, localesAgain, "languagesByCountry should be cached and return the same instance");
        assertUnmodifiableCollection(localesOnce);

        for (final String expectedLanguage : expectedLanguages) {
            boolean found = false;
            for (final Locale locale : localesOnce) {
                assertTrue(StringUtils.isEmpty(locale.getVariant()), "Expected empty variant");
                assertEquals(country, locale.getCountry(), "Country mismatch in returned locale");
                if (expectedLanguage.equals(locale.getLanguage())) {
                    found = true;
                    break;
                }
            }
            assertTrue(found, "Missing expected language '" + expectedLanguage + "' for country '" + country + "'");
        }
    }

    /**
     * Asserts the lookup chain for a given (locale, defaultLocale) pair is unmodifiable and matches expected.
     */
    private static void assertLocaleLookupChain(final Locale locale, final Locale defaultLocale, final Locale... expected) {
        final List<Locale> actual = defaultLocale == null
                ? LocaleUtils.localeLookupList(locale)
                : LocaleUtils.localeLookupList(locale, defaultLocale);

        assertEquals(expected.length, actual.size(), "Unexpected lookup chain length");
        assertEquals(Arrays.asList(expected), actual, "Unexpected lookup chain contents");
        assertUnmodifiableCollection(actual);
    }

    /**
     * Asserts that a collection is unmodifiable by provoking an UnsupportedOperationException on add.
     */
    private static void assertUnmodifiableCollection(final Collection<?> coll) {
        assertThrows(UnsupportedOperationException.class, () -> coll.add(null), "Collection should be unmodifiable");
    }

    /**
     * Convenience: Asserts that parsing the given string yields a Locale with the expected language only.
     * Expected country and variant are empty.
     */
    private static void assertParsesToLocaleLanguageOnly(final String language) {
        assertParsesToLocaleParts(language, language, "", "");
    }

    /**
     * Convenience: Asserts that parsing yields a Locale with expected language and country, empty variant.
     */
    private static void assertParsesToLocaleParts(final String localeString, final String language, final String country) {
        assertParsesToLocaleParts(localeString, language, country, "");
    }

    /**
     * Asserts that parsing yields a Locale with expected language, country, and variant.
     */
    private static void assertParsesToLocaleParts(final String localeString, final String language, final String country, final String variant) {
        final Locale locale = LocaleUtils.toLocale(localeString);
        assertNotNull(locale, "Expected a non-null Locale");
        assertEquals(language, locale.getLanguage(), "Language mismatch");
        assertEquals(country, locale.getCountry(), "Country mismatch");
        assertEquals(variant, locale.getVariant(), "Variant mismatch");
    }

    /**
     * Returns the given Locale's toString() with any script/extension suffix removed.
     * Locale#toString may include suffixes like "_#..." or "#...".
     */
    private static String removeLocaleSuffixAfterHash(final String str) {
        int idx = str.indexOf("_#");
        if (idx == -1) {
            idx = str.indexOf('#');
        }
        return idx >= 0 ? str.substring(0, idx) : str;
    }

    /**
     * Asserts that isAvailableLocale mirrors availableLocaleSet().contains(locale).
     */
    private static void assertAvailabilityConsistency(final Locale locale) {
        final boolean expected = LocaleUtils.availableLocaleSet().contains(locale);
        assertEquals(expected, LocaleUtils.isAvailableLocale(locale),
                "isAvailableLocale must reflect availableLocaleSet() membership");
    }

    @BeforeEach
    public void setUp() {
        // Testing #LANG-304. Must be called before availableLocaleSet is called.
        LocaleUtils.isAvailableLocale(Locale.getDefault());
    }

    @Test
    void testAvailableLocaleList() {
        final List<Locale> localesOnce = LocaleUtils.availableLocaleList();
        final List<Locale> localesAgain = LocaleUtils.availableLocaleList();

        assertNotNull(localesOnce);
        assertSame(localesOnce, localesAgain, "availableLocaleList should be cached and return the same instance");
        assertUnmodifiableCollection(localesOnce);

        final List<Locale> expected = Arrays.asList(
                ArraySorter.sort(Locale.getAvailableLocales(), Comparator.comparing(Locale::toString)));
        assertEquals(expected, localesOnce, "availableLocaleList should be sorted and reflect JVM locales");
    }

    @Test
    void testAvailableLocaleSet() {
        final Set<Locale> setOnce = LocaleUtils.availableLocaleSet();
        final Set<Locale> setAgain = LocaleUtils.availableLocaleSet();

        assertNotNull(setOnce);
        assertSame(setOnce, setAgain, "availableLocaleSet should be cached and return the same instance");
        assertUnmodifiableCollection(setOnce);

        final Set<Locale> expected = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        assertEquals(expected, setOnce, "availableLocaleSet should reflect JVM locales");
    }

    @Test
    void testConstructor() {
        assertNotNull(new LocaleUtils(), "Public no-arg constructor should exist (deprecated but present)");
        final Constructor<?>[] constructors = LocaleUtils.class.getDeclaredConstructors();
        assertEquals(1, constructors.length, "Expected a single constructor");
        assertTrue(Modifier.isPublic(constructors[0].getModifiers()), "Constructor should be public (deprecated)");
        assertTrue(Modifier.isPublic(LocaleUtils.class.getModifiers()), "Class should be public");
        assertFalse(Modifier.isFinal(LocaleUtils.class.getModifiers()), "Class should not be final");
    }

    @Test
    void testCountriesByLanguage() {
        assertCountriesByLanguageContains(null /* null => empty list */);
        assertCountriesByLanguageContains("de", "DE", "CH", "AT", "LU");
        assertCountriesByLanguageContains("zz" /* unknown language => empty list */);
        assertCountriesByLanguageContains("it", "IT", "CH");
    }

    @Test
    void testIsAvailableLocale() {
        assertAvailabilityConsistency(LOCALE_EN);
        assertAvailabilityConsistency(LOCALE_EN_US);
        assertAvailabilityConsistency(LOCALE_EN_US_ZZZZ);
        assertAvailabilityConsistency(LOCALE_FR);
        assertAvailabilityConsistency(LOCALE_FR_CA);
        assertAvailabilityConsistency(LOCALE_QQ);
        assertAvailabilityConsistency(LOCALE_QQ_ZZ);
    }

    @Test
    void testIsLanguageUndetermined() {
        final Set<Locale> available = LocaleUtils.availableLocaleSet();

        // Determined languages: available set membership is typically true, undetermined must be false.
        assertNotEquals(available.contains(LOCALE_EN), LocaleUtils.isLanguageUndetermined(LOCALE_EN));
        assertNotEquals(available.contains(LOCALE_EN_US), LocaleUtils.isLanguageUndetermined(LOCALE_EN_US));
        assertNotEquals(available.contains(LOCALE_FR), LocaleUtils.isLanguageUndetermined(LOCALE_FR));
        assertNotEquals(available.contains(LOCALE_FR_CA), LocaleUtils.isLanguageUndetermined(LOCALE_FR_CA));

        // Locales with either unknown language or empty language: typically not available, undetermined may be false or true.
        // For these specific cases, the current JVMs yield matching booleans.
        assertEquals(available.contains(LOCALE_EN_US_ZZZZ), LocaleUtils.isLanguageUndetermined(LOCALE_EN_US_ZZZZ));
        assertEquals(available.contains(LOCALE_QQ), LocaleUtils.isLanguageUndetermined(LOCALE_QQ));
        assertEquals(available.contains(LOCALE_QQ_ZZ), LocaleUtils.isLanguageUndetermined(LOCALE_QQ_ZZ));

        // Null => undetermined.
        assertTrue(LocaleUtils.isLanguageUndetermined(null));
    }

    /**
     * Tests #LANG-328 - language + variant (no country).
     */
    @Test
    void testLang328_languageAndVariantOnly() {
        assertParsesToLocaleParts("fr__P", "fr", "", "P");
        assertParsesToLocaleParts("fr__POSIX", "fr", "", "POSIX");
    }

    /**
     * Tests #LANG-865, strings starting with an underscore.
     */
    @Test
    void testLang865_underscorePrefixed() {
        assertParsesToLocaleParts("_GB", "", "GB");
        assertParsesToLocaleParts("_GB_P", "", "GB", "P");
        assertParsesToLocaleParts("_GB_POSIX", "", "GB", "POSIX");

        assertIllegalArgumentException(() -> LocaleUtils.toLocale("_G"), "Must be at least 3 chars if starts with underscore");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("_Gb"), "Must be uppercase if starts with underscore");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("_gB"), "Must be uppercase if starts with underscore");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("_1B"), "Must be letter if starts with underscore");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("_G1"), "Must be letter if starts with underscore");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("_GB_"), "Must be at least 5 chars if starts with underscore");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("_GBAP"),
                "Must have underscore after the country if starts with underscore and is at least 5 chars");
    }

    @Test
    void testLanguageAndUNM49Numeric3AreaCodeLang1312() {
        assertParsesToLocaleParts("en_001", "en", "001");
        assertParsesToLocaleParts("en_150", "en", "150");
        assertParsesToLocaleParts("ar_001", "ar", "001");

        // LANG-1312
        assertParsesToLocaleParts("en_001_GB", "en", "001", "GB");
        assertParsesToLocaleParts("en_150_US", "en", "150", "US");
    }

    @Test
    void testLanguagesByCountry() {
        assertLanguagesByCountryContains(null /* null => empty list */);
        assertLanguagesByCountryContains("GB", "en");
        assertLanguagesByCountryContains("ZZ" /* unknown country => empty list */);
        assertLanguagesByCountryContains("CH", "fr", "de", "it");
    }

    @Test
    void testLocaleLookupList_LocaleOnly() {
        assertLocaleLookupChain(null, null /* => empty */);
        assertLocaleLookupChain(LOCALE_QQ, null, LOCALE_QQ);
        assertLocaleLookupChain(LOCALE_EN, null, LOCALE_EN);
        assertLocaleLookupChain(LOCALE_EN_US, null, LOCALE_EN_US, LOCALE_EN);
        assertLocaleLookupChain(LOCALE_EN_US_ZZZZ, null, LOCALE_EN_US_ZZZZ, LOCALE_EN_US, LOCALE_EN);
    }

    @Test
    void testLocaleLookupList_LocaleWithDefault() {
        assertLocaleLookupChain(LOCALE_QQ, LOCALE_QQ, LOCALE_QQ);
        assertLocaleLookupChain(LOCALE_EN, LOCALE_EN, LOCALE_EN);

        assertLocaleLookupChain(LOCALE_EN_US, LOCALE_EN_US, LOCALE_EN_US, LOCALE_EN);
        assertLocaleLookupChain(LOCALE_EN_US, LOCALE_QQ, LOCALE_EN_US, LOCALE_EN, LOCALE_QQ);
        assertLocaleLookupChain(LOCALE_EN_US, LOCALE_QQ_ZZ, LOCALE_EN_US, LOCALE_EN, LOCALE_QQ_ZZ);

        assertLocaleLookupChain(LOCALE_EN_US_ZZZZ, null, LOCALE_EN_US_ZZZZ, LOCALE_EN_US, LOCALE_EN);
        assertLocaleLookupChain(LOCALE_EN_US_ZZZZ, LOCALE_EN_US_ZZZZ, LOCALE_EN_US_ZZZZ, LOCALE_EN_US, LOCALE_EN);
        assertLocaleLookupChain(LOCALE_EN_US_ZZZZ, LOCALE_QQ, LOCALE_EN_US_ZZZZ, LOCALE_EN_US, LOCALE_EN, LOCALE_QQ);
        assertLocaleLookupChain(LOCALE_EN_US_ZZZZ, LOCALE_QQ_ZZ, LOCALE_EN_US_ZZZZ, LOCALE_EN_US, LOCALE_EN, LOCALE_QQ_ZZ);
        assertLocaleLookupChain(LOCALE_FR_CA, LOCALE_EN, LOCALE_FR_CA, LOCALE_FR, LOCALE_EN);
    }

    @ParameterizedTest
    @MethodSource("java.util.Locale#getAvailableLocales")
    void testParseAllLocales(final Locale actualLocale) {
        // Only test Locales that can be recreated with the standard three-arg Locale constructor.
        final Locale roundTrip = new Locale(actualLocale.getLanguage(), actualLocale.getCountry(), actualLocale.getVariant());
        if (actualLocale.equals(roundTrip)) {
            final String original = actualLocale.toString();
            final String withoutSuffix = removeLocaleSuffixAfterHash(original);

            if (!original.equals(withoutSuffix)) {
                // If there was a suffix, toLocale must reject the original with suffix.
                assertIllegalArgumentException(() -> LocaleUtils.toLocale(original));
            }
            final Locale parsed = LocaleUtils.toLocale(withoutSuffix);
            assertEquals(actualLocale, parsed, "Parsing should reproduce the Locale");
        }
    }

    /**
     * Test for 3-letter language locales (#LANG-915).
     */
    @Test
    void testThreeLetterLanguageLocales() {
        for (final String lang : Arrays.asList("udm", "tet")) {
            final Locale locale = LocaleUtils.toLocale(lang);
            assertNotNull(locale);
            assertEquals(lang, locale.getLanguage());
            assertTrue(StringUtils.isBlank(locale.getCountry()));
            assertEquals(new Locale(lang), locale);
        }
    }

    @Test
    void testToLocale_1Part() {
        assertNull(LocaleUtils.toLocale((String) null));
        assertParsesToLocaleLanguageOnly("us");
        assertParsesToLocaleLanguageOnly("fr");
        assertParsesToLocaleLanguageOnly("de");
        assertParsesToLocaleLanguageOnly("zh");
        // Valid format but language doesn't exist, should still create the instance.
        assertParsesToLocaleLanguageOnly("qq");
        // LANG-941: JDK 8 introduced the empty locale as one of the default locales.
        assertParsesToLocaleLanguageOnly("");

        assertIllegalArgumentException(() -> LocaleUtils.toLocale("Us"), "Should fail if not lowercase");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("uS"), "Should fail if not lowercase");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("u#"), "Should fail if not lowercase");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("u"), "Must be 2 chars if less than 5");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("uu_U"), "Must be 2 chars if less than 5");
    }

    @Test
    void testToLocale_2Part() {
        assertParsesToLocaleParts("us_EN", "us", "EN");
        assertParsesToLocaleParts("us-EN", "us", "EN");
        // Valid even if it doesn't exist.
        assertParsesToLocaleParts("us_ZH", "us", "ZH");

        assertIllegalArgumentException(() -> LocaleUtils.toLocale("us_En"), "Should fail second part not uppercase");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("us_en"), "Should fail second part not uppercase");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("us_eN"), "Should fail second part not uppercase");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("uS_EN"), "Should fail first part not lowercase");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("us_E3"), "Should fail second part not uppercase");
    }

    @Test
    void testToLocale_3Part() {
        assertParsesToLocaleParts("us_EN_A", "us", "EN", "A");
        assertParsesToLocaleParts("us-EN-A", "us", "EN", "A");

        // JDK behavior difference: see https://bugs.java.com/bugdatabase/view_bug.do?bug_id=4210525
        if (SystemUtils.isJavaVersionAtLeast(JAVA_1_4)) {
            assertParsesToLocaleParts("us_EN_a", "us", "EN", "a");
            assertParsesToLocaleParts("us_EN_SFsafdFDsdfF", "us", "EN", "SFsafdFDsdfF");
        } else {
            assertParsesToLocaleParts("us_EN_a", "us", "EN", "A");
            assertParsesToLocaleParts("us_EN_SFsafdFDsdfF", "us", "EN", "SFSAFDFDSDFF");
        }

        assertIllegalArgumentException(() -> LocaleUtils.toLocale("us_EN-a"), "Should fail as no consistent delimiter");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("uu_UU_"), "Must be 3, 5 or 7+ in length");

        // LANG-1741
        assertEquals(new Locale("en", "001", "US_POSIX"), LocaleUtils.toLocale("en_001_US_POSIX"));
    }

    @Test
    void testToLocale_Locale_defaults() {
        assertNull(LocaleUtils.toLocale((String) null));
        assertEquals(Locale.getDefault(), LocaleUtils.toLocale((Locale) null));
        assertEquals(Locale.getDefault(), LocaleUtils.toLocale(Locale.getDefault()));
    }

    @ParameterizedTest
    @MethodSource("java.util.Locale#getAvailableLocales")
    void testToLocales(final Locale actualLocale) {
        assertEquals(actualLocale, LocaleUtils.toLocale(actualLocale));
    }
}