/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.lang3;

import static org.apache.commons.lang3.JavaVersion.JAVA_1_4;
import static org.apache.commons.lang3.LangAssertions.assertIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Unit tests for {@link LocaleUtils}.
 */
@DisplayName("Tests for LocaleUtils")
class LocaleUtilsRefactoredTest extends AbstractLangTest {

    private static final Locale LOCALE_EN = new Locale("en");
    private static final Locale LOCALE_EN_US = new Locale("en", "US");
    private static final Locale LOCALE_EN_US_ZZZZ = new Locale("en", "US", "ZZZZ");
    private static final Locale LOCALE_FR = new Locale("fr");
    private static final Locale LOCALE_FR_CA = new Locale("fr", "CA");
    private static final Locale LOCALE_QQ = new Locale("qq");
    private static final Locale LOCALE_QQ_ZZ = new Locale("qq", "ZZ");

    @BeforeEach
    void setUp() {
        // Testing #LANG-304. This must be called before availableLocaleSet is initialized.
        LocaleUtils.isAvailableLocale(Locale.getDefault());
    }

    @Test
    @DisplayName("The constructor should be public for JavaBean compatibility")
    void constructor_shouldBePublicAndExist() {
        assertNotNull(new LocaleUtils());
        final Constructor<?>[] cons = LocaleUtils.class.getDeclaredConstructors();
        assertEquals(1, cons.length);
        assertTrue(Modifier.isPublic(cons[0].getModifiers()));
        assertTrue(Modifier.isPublic(LocaleUtils.class.getModifiers()));
        assertFalse(Modifier.isFinal(LocaleUtils.class.getModifiers()));
    }

    @Nested
    @DisplayName("Available Locales Tests")
    class AvailableLocalesTests {

        @Test
        @DisplayName("availableLocaleList() should return an unmodifiable list of all JVM locales")
        void availableLocaleList_shouldReturnUnmodifiableListOfAllJvmLocales() {
            final List<Locale> list = LocaleUtils.availableLocaleList();
            final List<Locale> list2 = LocaleUtils.availableLocaleList();

            assertNotNull(list);
            assertSame(list, list2, "Should return the same cached list instance");
            assertUnmodifiableCollection(list);

            final Locale[] jdkLocaleArray = Locale.getAvailableLocales();
            final List<Locale> jdkLocaleList = Arrays.asList(ArraySorter.sort(jdkLocaleArray, Comparator.comparing(Locale::toString)));
            assertIterableEquals(jdkLocaleList, list);
        }

        @Test
        @DisplayName("availableLocaleSet() should return an unmodifiable set of all JVM locales")
        void availableLocaleSet_shouldReturnUnmodifiableSetOfAllJvmLocales() {
            final Set<Locale> set = LocaleUtils.availableLocaleSet();
            final Set<Locale> set2 = LocaleUtils.availableLocaleSet();

            assertNotNull(set);
            assertSame(set, set2, "Should return the same cached set instance");
            assertUnmodifiableCollection(set);

            final Set<Locale> jdkLocaleSet = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
            assertEquals(jdkLocaleSet, set);
        }

        @Test
        @DisplayName("isAvailableLocale() should correctly identify if a locale is available")
        void isAvailableLocale_shouldCorrectlyIdentifyIfLocaleIsAvailable() {
            final Set<Locale> set = LocaleUtils.availableLocaleSet();
            assertEquals(set.contains(LOCALE_EN), LocaleUtils.isAvailableLocale(LOCALE_EN));
            assertEquals(set.contains(LOCALE_EN_US), LocaleUtils.isAvailableLocale(LOCALE_EN_US));
            assertEquals(set.contains(LOCALE_EN_US_ZZZZ), LocaleUtils.isAvailableLocale(LOCALE_EN_US_ZZZZ));
            assertEquals(set.contains(LOCALE_FR), LocaleUtils.isAvailableLocale(LOCALE_FR));
            assertEquals(set.contains(LOCALE_FR_CA), LocaleUtils.isAvailableLocale(LOCALE_FR_CA));
            assertEquals(set.contains(LOCALE_QQ), LocaleUtils.isAvailableLocale(LOCALE_QQ));
            assertEquals(set.contains(LOCALE_QQ_ZZ), LocaleUtils.isAvailableLocale(LOCALE_QQ_ZZ));
        }
    }

    @Nested
    @DisplayName("Locale Lookup List Tests")
    class LocaleLookupListTests {

        @Test
        @DisplayName("localeLookupList(locale) should generate the correct fallback hierarchy")
        void localeLookupList_withSingleLocale_shouldReturnCorrectFallbackList() {
            assertLocaleLookupList(null, null, new Locale[0]);
            assertLocaleLookupList(LOCALE_QQ, null, new Locale[]{LOCALE_QQ});
            assertLocaleLookupList(LOCALE_EN, null, new Locale[]{LOCALE_EN});
            assertLocaleLookupList(LOCALE_EN_US, null, new Locale[]{LOCALE_EN_US, LOCALE_EN});
            assertLocaleLookupList(LOCALE_EN_US_ZZZZ, null, new Locale[]{LOCALE_EN_US_ZZZZ, LOCALE_EN_US, LOCALE_EN});
        }

        @Test
        @DisplayName("localeLookupList(locale, default) should append the default locale to the hierarchy")
        void localeLookupList_withDefaultLocale_shouldReturnCorrectFallbackList() {
            assertLocaleLookupList(LOCALE_EN_US, LOCALE_QQ, new Locale[]{LOCALE_EN_US, LOCALE_EN, LOCALE_QQ});
            assertLocaleLookupList(LOCALE_EN_US, LOCALE_EN_US, new Locale[]{LOCALE_EN_US, LOCALE_EN}); // No duplicates
            assertLocaleLookupList(LOCALE_EN_US_ZZZZ, LOCALE_QQ_ZZ, new Locale[]{LOCALE_EN_US_ZZZZ, LOCALE_EN_US, LOCALE_EN, LOCALE_QQ_ZZ});
            assertLocaleLookupList(LOCALE_FR_CA, LOCALE_EN, new Locale[]{LOCALE_FR_CA, LOCALE_FR, LOCALE_EN});
        }
    }

    @Nested
    @DisplayName("Country and Language List Tests")
    class CountryAndLanguageListTests {

        @Test
        @DisplayName("countriesByLanguage() should return correct countries for a given language")
        void countriesByLanguage_shouldReturnCorrectCountriesForGivenLanguage() {
            assertCountriesByLanguage(null, new String[0]);
            assertCountriesByLanguage("de", new String[]{"DE", "CH", "AT", "LU"});
            assertCountriesByLanguage("zz", new String[0]); // Invalid language
            assertCountriesByLanguage("it", new String[]{"IT", "CH"});
        }

        @Test
        @DisplayName("languagesByCountry() should return correct languages for a given country")
        void languagesByCountry_shouldReturnCorrectLanguagesForGivenCountry() {
            assertLanguageByCountry(null, new String[0]);
            assertLanguageByCountry("GB", new String[]{"en"});
            assertLanguageByCountry("ZZ", new String[0]); // Invalid country
            assertLanguageByCountry("CH", new String[]{"fr", "de", "it"});
        }
    }

    @Nested
    @DisplayName("toLocale(String) Tests")
    class ToLocaleFromStringTests {

        @Test
        @DisplayName("should return null for null input")
        void toLocale_shouldReturnNullForNullInput() {
            assertNull(LocaleUtils.toLocale((String) null));
        }

        @Test
        @DisplayName("should parse language-only strings")
        void toLocale_shouldParseLanguageOnlyStrings() {
            assertValidToLocale("us");
            assertValidToLocale("fr");
            assertValidToLocale("de");
            assertValidToLocale("zh");
            assertValidToLocale("qq"); // Valid format, even if language doesn't exist
            assertValidToLocale("");   // LANG-941: Empty locale
        }

        @Test
        @DisplayName("should parse language and country strings with underscore or dash separators")
        void toLocale_shouldParseLanguageAndCountryStrings() {
            assertValidToLocale("us_EN", "us", "EN");
            assertValidToLocale("us-EN", "us", "EN");
            assertValidToLocale("us_ZH", "us", "ZH"); // Valid format, even if combo doesn't exist
        }

        @Test
        @DisplayName("should parse language, country, and variant strings")
        void toLocale_shouldParseLanguageCountryAndVariantStrings() {
            assertValidToLocale("us_EN_A", "us", "EN", "A");
            assertValidToLocale("us-EN-A", "us", "EN", "A");

            // LANG-1741: Handle complex variants
            assertEquals(new Locale("en", "001", "US_POSIX"), LocaleUtils.toLocale("en_001_US_POSIX"));

            // This behavior changed between JDK 1.3 and 1.4
            if (SystemUtils.isJavaVersionAtLeast(JAVA_1_4)) {
                assertValidToLocale("us_EN_a", "us", "EN", "a");
                assertValidToLocale("us_EN_SFsafdFDsdfF", "us", "EN", "SFsafdFDsdfF");
            } else {
                assertValidToLocale("us_EN_a", "us", "EN", "A");
                assertValidToLocale("us_EN_SFsafdFDsdfF", "us", "EN", "SFSAFDFDSDFF");
            }
        }

        @Test
        @DisplayName("should parse locales with only language and variant (e.g., 'fr__POSIX')")
        void toLocale_shouldParseLocaleWithVariantOnly() {
            assertValidToLocale("fr__P", "fr", "", "P");
            assertValidToLocale("fr__POSIX", "fr", "", "POSIX");
        }

        @Test
        @DisplayName("should correctly parse 3-letter language codes")
        void toLocale_shouldParseThreeLetterLanguageCodes() {
            for (final String str : Arrays.asList("udm", "tet")) {
                final Locale locale = LocaleUtils.toLocale(str);
                assertNotNull(locale);
                assertEquals(str, locale.getLanguage());
                assertTrue(StringUtils.isBlank(locale.getCountry()));
                assertEquals(new Locale(str), locale);
            }
        }

        @Test
        @DisplayName("should correctly parse UN M.49 numeric area codes")
        void toLocale_shouldParseNumericAreaCodes() {
            assertValidToLocale("en_001", "en", "001");
            assertValidToLocale("en_150", "en", "150");
            assertValidToLocale("ar_001", "ar", "001");
            assertValidToLocale("en_001_GB", "en", "001", "GB"); // LANG-1312
            assertValidToLocale("en_150_US", "en", "150", "US"); // LANG-1312
        }

        @Test
        @DisplayName("should handle strings starting with an underscore (e.g., '_GB')")
        void toLocale_shouldHandleStringsStartingWithUnderscore() {
            assertValidToLocale("_GB", "", "GB", "");
            assertValidToLocale("_GB_P", "", "GB", "P");
            assertValidToLocale("_GB_POSIX", "", "GB", "POSIX");
        }

        @Test
        @DisplayName("should throw IllegalArgumentException for invalid formats")
        void toLocale_shouldThrowExceptionForInvalidFormats() {
            assertIllegalArgumentException(() -> LocaleUtils.toLocale("u"), "Must be 2 chars if less than 5");
            assertIllegalArgumentException(() -> LocaleUtils.toLocale("Us"), "Language part should be lowercase");
            assertIllegalArgumentException(() -> LocaleUtils.toLocale("us_En"), "Country part should be uppercase");
            assertIllegalArgumentException(() -> LocaleUtils.toLocale("us_E3"), "Country part must be letters");
            assertIllegalArgumentException(() -> LocaleUtils.toLocale("us_EN-a"), "Inconsistent separators");
            assertIllegalArgumentException(() -> LocaleUtils.toLocale("uu_UU_"), "Invalid length");
        }

        @Test
        @DisplayName("should throw IllegalArgumentException for invalid underscore-prefixed strings")
        void toLocale_shouldThrowExceptionForInvalidUnderscorePrefixedStrings() {
            assertIllegalArgumentException(() -> LocaleUtils.toLocale("_G"), "Must be at least 3 chars if starts with underscore");
            assertIllegalArgumentException(() -> LocaleUtils.toLocale("_Gb"), "Country part must be uppercase");
            assertIllegalArgumentException(() -> LocaleUtils.toLocale("_gB"), "Country part must be uppercase");
            assertIllegalArgumentException(() -> LocaleUtils.toLocale("_1B"), "Country part must be letters");
            assertIllegalArgumentException(() -> LocaleUtils.toLocale("_G1"), "Country part must be letters");
            assertIllegalArgumentException(() -> LocaleUtils.toLocale("_GB_"), "Must be at least 5 chars if variant is present");
            assertIllegalArgumentException(() -> LocaleUtils.toLocale("_GBAP"), "Must have underscore separator after country");
        }

        @ParameterizedTest
        @MethodSource("java.util.Locale#getAvailableLocales")
        @DisplayName("should successfully parse all available JVM locales")
        void toLocale_shouldRecreateAllAvailableJvmLocales(final Locale actualLocale) {
            // Check if the Locale can be recreated by the standard constructor.
            // If not, LocaleUtils.toLocale cannot be expected to handle it.
            final Locale constructedLocale = new Locale(actualLocale.getLanguage(), actualLocale.getCountry(), actualLocale.getVariant());
            if (!actualLocale.equals(constructedLocale)) {
                return;
            }

            final String str = actualLocale.toString();
            // LocaleUtils.toLocale does not support script/extension suffixes (e.g., "_#Latn").
            int suffixIndex = str.indexOf("_#");
            if (suffixIndex == -1) {
                suffixIndex = str.indexOf("#");
            }

            if (suffixIndex >= 0) {
                // Verify that strings with suffixes are rejected.
                assertIllegalArgumentException(() -> LocaleUtils.toLocale(str));
                // Test parsing by removing the unsupported suffix.
                String localeStr = str.substring(0, suffixIndex);
                assertEquals(actualLocale, LocaleUtils.toLocale(localeStr));
            } else {
                // No suffix, should parse directly.
                assertEquals(actualLocale, LocaleUtils.toLocale(str));
            }
        }
    }

    @Nested
    @DisplayName("toLocale(Locale) Tests")
    class ToLocaleFromLocaleTests {

        @Test
        @DisplayName("should return the default locale when input is null")
        void toLocale_shouldReturnDefaultLocaleForNullInput() {
            assertEquals(Locale.getDefault(), LocaleUtils.toLocale((Locale) null));
        }

        @ParameterizedTest
        @MethodSource("java.util.Locale#getAvailableLocales")
        @DisplayName("should return the same locale instance when a non-null locale is provided")
        void toLocale_shouldReturnSameInstanceForNonNullInput(final Locale actualLocale) {
            assertEquals(actualLocale, LocaleUtils.toLocale(actualLocale));
        }
    }

    @Test
    void isLanguageUndetermined_shouldCorrectlyIdentifyUndeterminedLanguages() {
        // Determined languages
        assertFalse(LocaleUtils.isLanguageUndetermined(LOCALE_EN));
        assertFalse(LocaleUtils.isLanguageUndetermined(LOCALE_EN_US));
        assertFalse(LocaleUtils.isLanguageUndetermined(LOCALE_FR));
        assertFalse(LocaleUtils.isLanguageUndetermined(LOCALE_FR_CA));

        // Undetermined languages
        assertTrue(LocaleUtils.isLanguageUndetermined(LOCALE_EN_US_ZZZZ));
        assertTrue(LocaleUtils.isLanguageUndetermined(LOCALE_QQ));
        assertTrue(LocaleUtils.isLanguageUndetermined(LOCALE_QQ_ZZ));
        assertTrue(LocaleUtils.isLanguageUndetermined(null));
    }

    // -----------------------------------------------------------------------
    // Helper Methods
    // -----------------------------------------------------------------------

    private static void assertUnmodifiableCollection(final Collection<?> coll) {
        assertThrows(UnsupportedOperationException.class, () -> coll.add(null), "Collection should be unmodifiable");
    }

    private static void assertLocaleLookupList(final Locale locale, final Locale defaultLocale, final Locale[] expected) {
        final List<Locale> localeList = defaultLocale == null ?
                LocaleUtils.localeLookupList(locale) :
                LocaleUtils.localeLookupList(locale, defaultLocale);
        assertIterableEquals(Arrays.asList(expected), localeList);
        assertUnmodifiableCollection(localeList);
    }

    private static void assertCountriesByLanguage(final String language, final String[] countries) {
        final List<Locale> list = LocaleUtils.countriesByLanguage(language);
        final List<Locale> list2 = LocaleUtils.countriesByLanguage(language);
        assertNotNull(list);
        assertSame(list, list2);

        final Set<String> actualCountries = new HashSet<>();
        for (final Locale locale : list) {
            assertTrue(StringUtils.isEmpty(locale.getVariant()));
            assertEquals(language, locale.getLanguage());
            actualCountries.add(locale.getCountry());
        }

        final Set<String> expectedCountries = new HashSet<>(Arrays.asList(countries));
        assertTrue(actualCountries.containsAll(expectedCountries),
                "Could not find all expected countries for language: " + language);
        assertUnmodifiableCollection(list);
    }

    private static void assertLanguageByCountry(final String country, final String[] languages) {
        final List<Locale> list = LocaleUtils.languagesByCountry(country);
        final List<Locale> list2 = LocaleUtils.languagesByCountry(country);
        assertNotNull(list);
        assertSame(list, list2);

        final Set<String> actualLanguages = new HashSet<>();
        for (final Locale locale : list) {
            assertTrue(StringUtils.isEmpty(locale.getVariant()));
            assertEquals(country, locale.getCountry());
            actualLanguages.add(locale.getLanguage());
        }

        final Set<String> expectedLanguages = new HashSet<>(Arrays.asList(languages));
        assertTrue(actualLanguages.containsAll(expectedLanguages),
                "Could not find all expected languages for country: " + country);
        assertUnmodifiableCollection(list);
    }

    private static void assertValidToLocale(final String localeString) {
        final Locale locale = LocaleUtils.toLocale(localeString);
        assertNotNull(locale, "valid locale");
        assertEquals(localeString, locale.getLanguage());
        assertTrue(StringUtils.isEmpty(locale.getCountry()));
        assertTrue(StringUtils.isEmpty(locale.getVariant()));
    }

    private static void assertValidToLocale(final String localeString, final String language, final String country) {
        final Locale locale = LocaleUtils.toLocale(localeString);
        assertNotNull(locale, "valid locale");
        assertEquals(language, locale.getLanguage());
        assertEquals(country, locale.getCountry());
        assertTrue(StringUtils.isEmpty(locale.getVariant()));
    }

    private static void assertValidToLocale(final String localeString, final String language, final String country, final String variant) {
        final Locale locale = LocaleUtils.toLocale(localeString);
        assertNotNull(locale, "valid locale");
        assertEquals(language, locale.getLanguage());
        assertEquals(country, locale.getCountry());
        assertEquals(variant, locale.getVariant());
    }
}