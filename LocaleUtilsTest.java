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
     * Verifies that countriesByLanguage returns expected countries for given language.
     */
    private static void assertCountriesForLanguage(final String language, final String[] expectedCountries) {
        final List<Locale> countries = LocaleUtils.countriesByLanguage(language);
        final List<Locale> cachedCountries = LocaleUtils.countriesByLanguage(language);
        assertNotNull(countries);
        assertSame(countries, cachedCountries);
        
        for (final String country : expectedCountries) {
            boolean found = false;
            for (final Locale locale : countries) {
                assertTrue(StringUtils.isEmpty(locale.getVariant()));
                assertEquals(language, locale.getLanguage());
                if (country.equals(locale.getCountry())) {
                    found = true;
                    break;
                }
            }
            assertTrue(found, "Country not found for language: " + country);
        }
        assertCollectionIsUnmodifiable(countries);
    }

    /**
     * Verifies that languagesByCountry returns expected languages for given country.
     */
    private static void assertLanguagesForCountry(final String country, final String[] expectedLanguages) {
        final List<Locale> languages = LocaleUtils.languagesByCountry(country);
        final List<Locale> cachedLanguages = LocaleUtils.languagesByCountry(country);
        assertNotNull(languages);
        assertSame(languages, cachedLanguages);
        
        for (final String language : expectedLanguages) {
            boolean found = false;
            for (final Locale locale : languages) {
                assertTrue(StringUtils.isEmpty(locale.getVariant()));
                assertEquals(country, locale.getCountry());
                if (language.equals(locale.getLanguage())) {
                    found = true;
                    break;
                }
            }
            assertTrue(found, "Language not found for country: " + language);
        }
        assertCollectionIsUnmodifiable(languages);
    }

    /**
     * Verifies locale lookup list matches expected sequence.
     */
    private static void assertLocaleLookupSequence(final Locale locale, final Locale defaultLocale, final Locale[] expectedSequence) {
        final List<Locale> lookupList = defaultLocale != null ?
                LocaleUtils.localeLookupList(locale, defaultLocale) :
                LocaleUtils.localeLookupList(locale);

        assertEquals(expectedSequence.length, lookupList.size());
        assertEquals(Arrays.asList(expectedSequence), lookupList);
        assertCollectionIsUnmodifiable(lookupList);
    }

    /**
     * Verifies collection is unmodifiable.
     */
    private static void assertCollectionIsUnmodifiable(final Collection<?> collection) {
        assertThrows(UnsupportedOperationException.class, () -> collection.add(null));
    }

    /**
     * Verifies valid language string converts to expected locale.
     */
    private static void assertValidLocaleConversion(final String localeString, final String expectedLanguage) {
        final Locale locale = LocaleUtils.toLocale(localeString);
        assertNotNull(locale, "Valid locale should not be null");
        assertEquals(expectedLanguage, locale.getLanguage());
        assertTrue(StringUtils.isEmpty(locale.getCountry()));
        assertTrue(StringUtils.isEmpty(locale.getVariant()));
    }

    /**
     * Verifies valid locale string converts to expected language/country locale.
     */
    private static void assertValidLocaleConversion(final String localeString, final String expectedLanguage, final String expectedCountry) {
        final Locale locale = LocaleUtils.toLocale(localeString);
        assertNotNull(locale, "Valid locale should not be null");
        assertEquals(expectedLanguage, locale.getLanguage());
        assertEquals(expectedCountry, locale.getCountry());
        assertTrue(StringUtils.isEmpty(locale.getVariant()));
    }

    /**
     * Verifies valid locale string converts to expected full locale.
     */
    private static void assertValidLocaleConversion(final String localeString, final String expectedLanguage,
                                                   final String expectedCountry, final String expectedVariant) {
        final Locale locale = LocaleUtils.toLocale(localeString);
        assertNotNull(locale, "Valid locale should not be null");
        assertEquals(expectedLanguage, locale.getLanguage());
        assertEquals(expectedCountry, locale.getCountry());
        assertEquals(expectedVariant, locale.getVariant());
    }

    @BeforeEach
    void setUp() {
        // Force initialization of available locales
        LocaleUtils.isAvailableLocale(Locale.getDefault());
    }

    @Test
    void availableLocaleList_returnsUnmodifiableListMatchingJdkLocales() {
        final List<Locale> locales = LocaleUtils.availableLocaleList();
        final List<Locale> cachedLocales = LocaleUtils.availableLocaleList();
        assertNotNull(locales);
        assertSame(locales, cachedLocales);
        assertCollectionIsUnmodifiable(locales);

        final Locale[] jdkLocales = Locale.getAvailableLocales();
        final List<Locale> sortedJdkLocales = Arrays.asList(ArraySorter.sort(jdkLocales, Comparator.comparing(Locale::toString)));
        assertEquals(sortedJdkLocales, locales);
    }

    @Test
    void availableLocaleSet_returnsUnmodifiableSetMatchingJdkLocales() {
        final Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
        final Set<Locale> cachedLocaleSet = LocaleUtils.availableLocaleSet();
        assertNotNull(localeSet);
        assertSame(localeSet, cachedLocaleSet);
        assertCollectionIsUnmodifiable(localeSet);

        final Set<Locale> jdkLocaleSet = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        assertEquals(jdkLocaleSet, localeSet);
    }

    @Test
    void constructor_isPublicAndSingleton() {
        assertNotNull(new LocaleUtils());
        final Constructor<?>[] constructors = LocaleUtils.class.getDeclaredConstructors();
        assertEquals(1, constructors.length);
        assertTrue(Modifier.isPublic(constructors[0].getModifiers()));
        assertTrue(Modifier.isPublic(LocaleUtils.class.getModifiers()));
        assertFalse(Modifier.isFinal(LocaleUtils.class.getModifiers()));
    }

    @Test
    void countriesByLanguage_returnsExpectedCountriesForGivenLanguages() {
        assertCountriesForLanguage(null, new String[0]);
        assertCountriesForLanguage("de", new String[]{"DE", "CH", "AT", "LU"});
        assertCountriesForLanguage("zz", new String[0]);
        assertCountriesForLanguage("it", new String[]{"IT", "CH"});
    }

    @Test
    void isAvailableLocale_matchesAvailableLocaleSet() {
        final Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        
        assertEquals(availableLocales.contains(LOCALE_EN), LocaleUtils.isAvailableLocale(LOCALE_EN));
        assertEquals(availableLocales.contains(LOCALE_EN_US), LocaleUtils.isAvailableLocale(LOCALE_EN_US));
        assertEquals(availableLocales.contains(LOCALE_EN_US_ZZZZ), LocaleUtils.isAvailableLocale(LOCALE_EN_US_ZZZZ));
        assertEquals(availableLocales.contains(LOCALE_FR), LocaleUtils.isAvailableLocale(LOCALE_FR));
        assertEquals(availableLocales.contains(LOCALE_FR_CA), LocaleUtils.isAvailableLocale(LOCALE_FR_CA));
        assertEquals(availableLocales.contains(LOCALE_QQ), LocaleUtils.isAvailableLocale(LOCALE_QQ));
        assertEquals(availableLocales.contains(LOCALE_QQ_ZZ), LocaleUtils.isAvailableLocale(LOCALE_QQ_ZZ));
    }

    @Test
    void isLanguageUndetermined_returnsCorrectStatusForLocales() {
        // Locales with determined languages
        assertFalse(LocaleUtils.isLanguageUndetermined(LOCALE_EN));
        assertFalse(LocaleUtils.isLanguageUndetermined(LOCALE_EN_US));
        assertFalse(LocaleUtils.isLanguageUndetermined(LOCALE_FR));
        assertFalse(LocaleUtils.isLanguageUndetermined(LOCALE_FR_CA));

        // Locales with undetermined languages
        assertTrue(LocaleUtils.isLanguageUndetermined(LOCALE_EN_US_ZZZZ));
        assertTrue(LocaleUtils.isLanguageUndetermined(LOCALE_QQ));
        assertTrue(LocaleUtils.isLanguageUndetermined(LOCALE_QQ_ZZ));

        // Null case
        assertTrue(LocaleUtils.isLanguageUndetermined(null));
    }

    @Test
    void toLocale_supportsLanguageWithVariantOnly() {
        assertValidLocaleConversion("fr__P", "fr", "", "P");
        assertValidLocaleConversion("fr__POSIX", "fr", "", "POSIX");
    }

    @Test
    void toLocale_supportsStringsStartingWithUnderscore() {
        assertValidLocaleConversion("_GB", "", "GB", "");
        assertValidLocaleConversion("_GB_P", "", "GB", "P");
        assertValidLocaleConversion("_GB_POSIX", "", "GB", "POSIX");
    }

    @Test
    void toLocale_throwsExceptionForInvalidUnderscoreStrings() {
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
    void toLocale_supportsUnM49NumericAreaCodes() {
        assertValidLocaleConversion("en_001", "en", "001");
        assertValidLocaleConversion("en_150", "en", "150");
        assertValidLocaleConversion("ar_001", "ar", "001");
        assertValidLocaleConversion("en_001_GB", "en", "001", "GB");
        assertValidLocaleConversion("en_150_US", "en", "150", "US");
    }

    @Test
    void languagesByCountry_returnsExpectedLanguagesForGivenCountries() {
        assertLanguagesForCountry(null, new String[0]);
        assertLanguagesForCountry("GB", new String[]{"en"});
        assertLanguagesForCountry("ZZ", new String[0]);
        assertLanguagesForCountry("CH", new String[]{"fr", "de", "it"});
    }

    @Test
    void localeLookupList_withSingleLocale_returnsCorrectSequence() {
        assertLocaleLookupSequence(null, null, new Locale[0]);
        assertLocaleLookupSequence(LOCALE_QQ, null, new Locale[]{LOCALE_QQ});
        assertLocaleLookupSequence(LOCALE_EN, null, new Locale[]{LOCALE_EN});
        assertLocaleLookupSequence(LOCALE_EN_US, null, new Locale[]{LOCALE_EN_US, LOCALE_EN});
        assertLocaleLookupSequence(LOCALE_EN_US_ZZZZ, null, 
            new Locale[]{LOCALE_EN_US_ZZZZ, LOCALE_EN_US, LOCALE_EN});
    }

    @Test
    void localeLookupList_withLocaleAndDefault_returnsCorrectSequence() {
        // Same locale and default
        assertLocaleLookupSequence(LOCALE_QQ, LOCALE_QQ, new Locale[]{LOCALE_QQ});
        assertLocaleLookupSequence(LOCALE_EN, LOCALE_EN, new Locale[]{LOCALE_EN});
        
        // Different locale and default
        assertLocaleLookupSequence(LOCALE_EN_US, LOCALE_EN_US, 
            new Locale[]{LOCALE_EN_US, LOCALE_EN});
        assertLocaleLookupSequence(LOCALE_EN_US, LOCALE_QQ, 
            new Locale[]{LOCALE_EN_US, LOCALE_EN, LOCALE_QQ});
        assertLocaleLookupSequence(LOCALE_EN_US, LOCALE_QQ_ZZ, 
            new Locale[]{LOCALE_EN_US, LOCALE_EN, LOCALE_QQ_ZZ});
            
        // With variant
        assertLocaleLookupSequence(LOCALE_EN_US_ZZZZ, LOCALE_QQ, 
            new Locale[]{LOCALE_EN_US_ZZZZ, LOCALE_EN_US, LOCALE_EN, LOCALE_QQ});
        assertLocaleLookupSequence(LOCALE_EN_US_ZZZZ, LOCALE_QQ_ZZ, 
            new Locale[]{LOCALE_EN_US_ZZZZ, LOCALE_EN_US, LOCALE_EN, LOCALE_QQ_ZZ});
            
        // Different language tree
        assertLocaleLookupSequence(LOCALE_FR_CA, LOCALE_EN, 
            new Locale[]{LOCALE_FR_CA, LOCALE_FR, LOCALE_EN});
    }

    @ParameterizedTest
    @MethodSource("java.util.Locale#getAvailableLocales")
    void toLocale_canParseAllAvailableLocalesWithoutExtensions(final Locale jdkLocale) {
        // Skip locales with script/extension suffix
        final String localeStr = jdkLocale.toString();
        if (localeStr.contains("_#") || localeStr.contains("#")) {
            return;
        }

        final Locale recreatedLocale = new Locale(
            jdkLocale.getLanguage(),
            jdkLocale.getCountry(),
            jdkLocale.getVariant()
        );
        
        if (jdkLocale.equals(recreatedLocale)) {
            final Locale parsedLocale = LocaleUtils.toLocale(localeStr);
            assertEquals(jdkLocale, parsedLocale);
        }
    }

    @Test
    void toLocale_supportsThreeCharacterLanguages() {
        for (final String language : Arrays.asList("udm", "tet")) {
            final Locale locale = LocaleUtils.toLocale(language);
            assertNotNull(locale);
            assertEquals(language, locale.getLanguage());
            assertTrue(StringUtils.isBlank(locale.getCountry()));
            assertEquals(new Locale(language), locale);
        }
    }

    @Test
    void toLocale_withLanguageOnly_validAndInvalidCases() {
        // Valid cases
        assertValidLocaleConversion("us", "us");
        assertValidLocaleConversion("fr", "fr");
        assertValidLocaleConversion("de", "de");
        assertValidLocaleConversion("zh", "zh");
        assertValidLocaleConversion("qq", "qq");
        assertValidLocaleConversion("", "");

        // Invalid cases
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("Us"), "Should fail if not lowercase");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("uS"), "Should fail if not lowercase");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("u#"), "Should fail if not lowercase");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("u"), "Must be 2 chars if less than 5");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("uu_U"), "Must be 2 chars if less than 5");
    }

    @Test
    void toLocale_withLanguageAndCountry_validAndInvalidCases() {
        // Valid cases
        assertValidLocaleConversion("us_EN", "us", "EN");
        assertValidLocaleConversion("us-EN", "us", "EN");
        assertValidLocaleConversion("us_ZH", "us", "ZH");

        // Invalid cases
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("us_En"), "Should fail second part not uppercase");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("us_en"), "Should fail second part not uppercase");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("us_eN"), "Should fail second part not uppercase");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("uS_EN"), "Should fail first part not lowercase");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("us_E3"), "Should fail second part not uppercase");
    }

    @Test
    void toLocale_withLanguageCountryAndVariant_validAndInvalidCases() {
        // Valid cases
        assertValidLocaleConversion("us_EN_A", "us", "EN", "A");
        assertValidLocaleConversion("us-EN-A", "us", "EN", "A");

        if (SystemUtils.isJavaVersionAtLeast(JAVA_1_4)) {
            assertValidLocaleConversion("us_EN_a", "us", "EN", "a");
            assertValidLocaleConversion("us_EN_SFsafdFDsdfF", "us", "EN", "SFsafdFDsdfF");
        } else {
            assertValidLocaleConversion("us_EN_a", "us", "EN", "A");
            assertValidLocaleConversion("us_EN_SFsafdFDsdfF", "us", "EN", "SFSAFDFDSDFF");
        }

        // LANG-1741: Extended variant format
        assertEquals(new Locale("en", "001", "US_POSIX"), LocaleUtils.toLocale("en_001_US_POSIX"));

        // Invalid cases
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("us_EN-a"), "Should fail as no consistent delimiter");
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("uu_UU_"), "Must be 3, 5 or 7+ in length");
    }

    @Test
    void toLocale_withLocaleObject_handlesNulls() {
        assertNull(LocaleUtils.toLocale((String) null));
        assertEquals(Locale.getDefault(), LocaleUtils.toLocale((Locale) null));
        assertEquals(Locale.getDefault(), LocaleUtils.toLocale(Locale.getDefault()));
    }

    @ParameterizedTest
    @MethodSource("java.util.Locale#getAvailableLocales")
    void toLocale_withLocaleObject_returnsSameObject(final Locale locale) {
        assertEquals(locale, LocaleUtils.toLocale(locale));
    }
}