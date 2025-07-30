package org.apache.commons.lang3;

import static org.apache.commons.lang3.JavaVersion.JAVA_1_4;
import static org.apache.commons.lang3.LangAssertions.assertIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Unit tests for {@link LocaleUtils}.
 */
class LocaleUtilsTest extends AbstractLangTest {

    // Predefined Locale constants for testing
    private static final Locale LOCALE_EN = new Locale("en", "");
    private static final Locale LOCALE_EN_US = new Locale("en", "US");
    private static final Locale LOCALE_EN_US_ZZZZ = new Locale("en", "US", "ZZZZ");
    private static final Locale LOCALE_FR = new Locale("fr", "");
    private static final Locale LOCALE_FR_CA = new Locale("fr", "CA");
    private static final Locale LOCALE_QQ = new Locale("qq", "");
    private static final Locale LOCALE_QQ_ZZ = new Locale("qq", "ZZ");

    @BeforeEach
    public void setUp() {
        // Ensure default locale is available before tests
        LocaleUtils.isAvailableLocale(Locale.getDefault());
    }

    /**
     * Test the list of available locales.
     */
    @Test
    void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        assertNotNull(locales, "Locale list should not be null");
        assertUnmodifiableCollection(locales);

        Locale[] jdkLocales = Locale.getAvailableLocales();
        List<Locale> expectedLocales = Arrays.asList(ArraySorter.sort(jdkLocales, Comparator.comparing(Locale::toString)));
        assertEquals(expectedLocales, locales, "Locale list should match JDK available locales");
    }

    /**
     * Test the set of available locales.
     */
    @Test
    void testAvailableLocaleSet() {
        Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
        assertNotNull(localeSet, "Locale set should not be null");
        assertUnmodifiableCollection(localeSet);

        Set<Locale> expectedLocaleSet = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        assertEquals(expectedLocaleSet, localeSet, "Locale set should match JDK available locales");
    }

    /**
     * Test the constructor of LocaleUtils.
     */
    @Test
    void testConstructor() {
        assertNotNull(new LocaleUtils(), "LocaleUtils instance should be created");
        Constructor<?>[] constructors = LocaleUtils.class.getDeclaredConstructors();
        assertEquals(1, constructors.length, "LocaleUtils should have one constructor");
        assertTrue(Modifier.isPublic(constructors[0].getModifiers()), "Constructor should be public");
    }

    /**
     * Test countriesByLanguage() method.
     */
    @Test
    void testCountriesByLanguage() {
        assertCountriesByLanguage(null, new String[0]);
        assertCountriesByLanguage("de", new String[]{"DE", "CH", "AT", "LU"});
        assertCountriesByLanguage("zz", new String[0]);
        assertCountriesByLanguage("it", new String[]{"IT", "CH"});
    }

    /**
     * Test isAvailableLocale() method.
     */
    @Test
    void testIsAvailableLocale() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        assertEquals(availableLocales.contains(LOCALE_EN), LocaleUtils.isAvailableLocale(LOCALE_EN));
        assertEquals(availableLocales.contains(LOCALE_EN_US), LocaleUtils.isAvailableLocale(LOCALE_EN_US));
        assertEquals(availableLocales.contains(LOCALE_EN_US_ZZZZ), LocaleUtils.isAvailableLocale(LOCALE_EN_US_ZZZZ));
        assertEquals(availableLocales.contains(LOCALE_FR), LocaleUtils.isAvailableLocale(LOCALE_FR));
        assertEquals(availableLocales.contains(LOCALE_FR_CA), LocaleUtils.isAvailableLocale(LOCALE_FR_CA));
        assertEquals(availableLocales.contains(LOCALE_QQ), LocaleUtils.isAvailableLocale(LOCALE_QQ));
        assertEquals(availableLocales.contains(LOCALE_QQ_ZZ), LocaleUtils.isAvailableLocale(LOCALE_QQ_ZZ));
    }

    /**
     * Test isLanguageUndetermined() method.
     */
    @Test
    void testIsLanguageUndetermined() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        assertNotEquals(availableLocales.contains(LOCALE_EN), LocaleUtils.isLanguageUndetermined(LOCALE_EN));
        assertNotEquals(availableLocales.contains(LOCALE_EN_US), LocaleUtils.isLanguageUndetermined(LOCALE_EN_US));
        assertNotEquals(availableLocales.contains(LOCALE_FR), LocaleUtils.isLanguageUndetermined(LOCALE_FR));
        assertNotEquals(availableLocales.contains(LOCALE_FR_CA), LocaleUtils.isLanguageUndetermined(LOCALE_FR_CA));
        assertEquals(availableLocales.contains(LOCALE_EN_US_ZZZZ), LocaleUtils.isLanguageUndetermined(LOCALE_EN_US_ZZZZ));
        assertEquals(availableLocales.contains(LOCALE_QQ), LocaleUtils.isLanguageUndetermined(LOCALE_QQ));
        assertEquals(availableLocales.contains(LOCALE_QQ_ZZ), LocaleUtils.isLanguageUndetermined(LOCALE_QQ_ZZ));
        assertTrue(LocaleUtils.isLanguageUndetermined(null), "Null should be considered undetermined");
    }

    /**
     * Test languagesByCountry() method.
     */
    @Test
    void testLanguagesByCountry() {
        assertLanguageByCountry(null, new String[0]);
        assertLanguageByCountry("GB", new String[]{"en"});
        assertLanguageByCountry("ZZ", new String[0]);
        assertLanguageByCountry("CH", new String[]{"fr", "de", "it"});
    }

    /**
     * Test localeLookupList() method with a single locale.
     */
    @Test
    void testLocaleLookupList_Locale() {
        assertLocaleLookupList(null, null, new Locale[0]);
        assertLocaleLookupList(LOCALE_QQ, null, new Locale[]{LOCALE_QQ});
        assertLocaleLookupList(LOCALE_EN, null, new Locale[]{LOCALE_EN});
        assertLocaleLookupList(LOCALE_EN_US, null, new Locale[]{LOCALE_EN_US, LOCALE_EN});
        assertLocaleLookupList(LOCALE_EN_US_ZZZZ, null, new Locale[]{LOCALE_EN_US_ZZZZ, LOCALE_EN_US, LOCALE_EN});
    }

    /**
     * Test localeLookupList() method with a default locale.
     */
    @Test
    void testLocaleLookupList_LocaleLocale() {
        assertLocaleLookupList(LOCALE_QQ, LOCALE_QQ, new Locale[]{LOCALE_QQ});
        assertLocaleLookupList(LOCALE_EN, LOCALE_EN, new Locale[]{LOCALE_EN});
        assertLocaleLookupList(LOCALE_EN_US, LOCALE_EN_US, new Locale[]{LOCALE_EN_US, LOCALE_EN});
        assertLocaleLookupList(LOCALE_EN_US, LOCALE_QQ, new Locale[]{LOCALE_EN_US, LOCALE_EN, LOCALE_QQ});
        assertLocaleLookupList(LOCALE_EN_US, LOCALE_QQ_ZZ, new Locale[]{LOCALE_EN_US, LOCALE_EN, LOCALE_QQ_ZZ});
        assertLocaleLookupList(LOCALE_EN_US_ZZZZ, null, new Locale[]{LOCALE_EN_US_ZZZZ, LOCALE_EN_US, LOCALE_EN});
        assertLocaleLookupList(LOCALE_EN_US_ZZZZ, LOCALE_QQ, new Locale[]{LOCALE_EN_US_ZZZZ, LOCALE_EN_US, LOCALE_EN, LOCALE_QQ});
        assertLocaleLookupList(LOCALE_FR_CA, LOCALE_EN, new Locale[]{LOCALE_FR_CA, LOCALE_FR, LOCALE_EN});
    }

    /**
     * Test parsing all available locales.
     */
    @ParameterizedTest
    @MethodSource("java.util.Locale#getAvailableLocales")
    void testParseAllLocales(final Locale actualLocale) {
        Locale locale = new Locale(actualLocale.getLanguage(), actualLocale.getCountry(), actualLocale.getVariant());
        if (actualLocale.equals(locale)) {
            String str = actualLocale.toString();
            int suffixIndex = str.indexOf("_#");
            if (suffixIndex == -1) {
                suffixIndex = str.indexOf("#");
            }
            String localeStr = str;
            if (suffixIndex >= 0) {
                assertIllegalArgumentException(() -> LocaleUtils.toLocale(str));
                localeStr = str.substring(0, suffixIndex);
            }
            Locale parsedLocale = LocaleUtils.toLocale(localeStr);
            assertEquals(actualLocale, parsedLocale);
        }
    }

    /**
     * Test for 3-character language codes.
     */
    @Test
    void testThreeCharsLocale() {
        List<String> threeCharLangs = Arrays.asList("udm", "tet");
        for (String lang : threeCharLangs) {
            Locale locale = LocaleUtils.toLocale(lang);
            assertNotNull(locale, "Locale should not be null");
            assertEquals(lang, locale.getLanguage(), "Language should match");
            assertTrue(StringUtils.isBlank(locale.getCountry()), "Country should be blank");
            assertEquals(new Locale(lang), locale, "Locale should match expected");
        }
    }

    /**
     * Test toLocale(String) method with single part.
     */
    @Test
    void testToLocale_1Part() {
        assertNull(LocaleUtils.toLocale((String) null), "Null input should return null");
        assertValidToLocale("us");
        assertValidToLocale("fr");
        assertValidToLocale("de");
        assertValidToLocale("zh");
        assertValidToLocale("qq");
        assertValidToLocale("");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("Us"), "Should fail if not lowercase");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("uS"), "Should fail if not lowercase");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("u#"), "Should fail if not lowercase");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("u"), "Must be 2 chars if less than 5");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("uu_U"), "Must be 2 chars if less than 5");
    }

    /**
     * Test toLocale(String) method with two parts.
     */
    @Test
    void testToLocale_2Part() {
        assertValidToLocale("us_EN", "us", "EN");
        assertValidToLocale("us-EN", "us", "EN");
        assertValidToLocale("us_ZH", "us", "ZH");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("us_En"), "Should fail second part not uppercase");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("us_en"), "Should fail second part not uppercase");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("us_eN"), "Should fail second part not uppercase");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("uS_EN"), "Should fail first part not lowercase");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("us_E3"), "Should fail second part not uppercase");
    }

    /**
     * Test toLocale(String) method with three parts.
     */
    @Test
    void testToLocale_3Part() {
        assertValidToLocale("us_EN_A", "us", "EN", "A");
        assertValidToLocale("us-EN-A", "us", "EN", "A");
        if (SystemUtils.isJavaVersionAtLeast(JAVA_1_4)) {
            assertValidToLocale("us_EN_a", "us", "EN", "a");
            assertValidToLocale("us_EN_SFsafdFDsdfF", "us", "EN", "SFsafdFDsdfF");
        } else {
            assertValidToLocale("us_EN_a", "us", "EN", "A");
            assertValidToLocale("us_EN_SFsafdFDsdfF", "us", "EN", "SFSAFDFDSDFF");
        }
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("us_EN-a"), "Should fail as no consistent delimiter");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("uu_UU_"), "Must be 3, 5 or 7+ in length");
        assertEquals(new Locale("en", "001", "US_POSIX"), LocaleUtils.toLocale("en_001_US_POSIX"));
    }

    /**
     * Test toLocale(Locale) method with defaults.
     */
    @Test
    void testToLocale_Locale_defaults() {
        assertNull(LocaleUtils.toLocale((String) null), "Null input should return null");
        assertEquals(Locale.getDefault(), LocaleUtils.toLocale((Locale) null), "Null locale should return default");
        assertEquals(Locale.getDefault(), LocaleUtils.toLocale(Locale.getDefault()), "Default locale should return itself");
    }

    /**
     * Test toLocale(Locale) method with available locales.
     */
    @ParameterizedTest
    @MethodSource("java.util.Locale#getAvailableLocales")
    void testToLocales(final Locale actualLocale) {
        assertEquals(actualLocale, LocaleUtils.toLocale(actualLocale), "Locale should match itself");
    }

    // Helper methods for assertions

    private static void assertCountriesByLanguage(final String language, final String[] countries) {
        List<Locale> locales = LocaleUtils.countriesByLanguage(language);
        assertNotNull(locales, "Locales should not be null");
        assertUnmodifiableCollection(locales);

        for (String country : countries) {
            boolean found = locales.stream().anyMatch(locale -> {
                assertTrue(StringUtils.isEmpty(locale.getVariant()), "Variant should be empty");
                assertEquals(language, locale.getLanguage(), "Language should match");
                return country.equals(locale.getCountry());
            });
            assertTrue(found, "Country " + country + " should be found for language " + language);
        }
    }

    private static void assertLanguageByCountry(final String country, final String[] languages) {
        List<Locale> locales = LocaleUtils.languagesByCountry(country);
        assertNotNull(locales, "Locales should not be null");
        assertUnmodifiableCollection(locales);

        for (String language : languages) {
            boolean found = locales.stream().anyMatch(locale -> {
                assertTrue(StringUtils.isEmpty(locale.getVariant()), "Variant should be empty");
                assertEquals(country, locale.getCountry(), "Country should match");
                return language.equals(locale.getLanguage());
            });
            assertTrue(found, "Language " + language + " should be found for country " + country);
        }
    }

    private static void assertLocaleLookupList(final Locale locale, final Locale defaultLocale, final Locale[] expected) {
        List<Locale> localeList = defaultLocale == null ?
                LocaleUtils.localeLookupList(locale) :
                LocaleUtils.localeLookupList(locale, defaultLocale);

        assertEquals(expected.length, localeList.size(), "Locale list size should match");
        assertEquals(Arrays.asList(expected), localeList, "Locale list should match expected");
        assertUnmodifiableCollection(localeList);
    }

    private static void assertUnmodifiableCollection(final Collection<?> collection) {
        assertThrows(UnsupportedOperationException.class, () -> collection.add(null), "Collection should be unmodifiable");
    }

    private static void assertValidToLocale(final String localeString) {
        Locale locale = LocaleUtils.toLocale(localeString);
        assertNotNull(locale, "Locale should not be null");
        assertEquals(localeString, locale.getLanguage(), "Language should match");
        assertTrue(StringUtils.isEmpty(locale.getCountry()), "Country should be empty");
        assertTrue(StringUtils.isEmpty(locale.getVariant()), "Variant should be empty");
    }

    private static void assertValidToLocale(final String localeString, final String language, final String country) {
        Locale locale = LocaleUtils.toLocale(localeString);
        assertNotNull(locale, "Locale should not be null");
        assertEquals(language, locale.getLanguage(), "Language should match");
        assertEquals(country, locale.getCountry(), "Country should match");
        assertTrue(StringUtils.isEmpty(locale.getVariant()), "Variant should be empty");
    }

    private static void assertValidToLocale(final String localeString, final String language, final String country, final String variant) {
        Locale locale = LocaleUtils.toLocale(localeString);
        assertNotNull(locale, "Locale should not be null");
        assertEquals(language, locale.getLanguage(), "Language should match");
        assertEquals(country, locale.getCountry(), "Country should match");
        assertEquals(variant, locale.getVariant(), "Variant should match");
    }
}