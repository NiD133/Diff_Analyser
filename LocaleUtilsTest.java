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

    // Test locale constants for better readability and reuse
    private static final Locale ENGLISH = new Locale("en", "");
    private static final Locale ENGLISH_US = new Locale("en", "US");
    private static final Locale ENGLISH_US_WITH_VARIANT = new Locale("en", "US", "ZZZZ");
    private static final Locale FRENCH = new Locale("fr", "");
    private static final Locale FRENCH_CANADA = new Locale("fr", "CA");
    private static final Locale UNKNOWN_LANGUAGE = new Locale("qq", "");
    private static final Locale UNKNOWN_LANGUAGE_AND_COUNTRY = new Locale("qq", "ZZ");

    @BeforeEach
    public void setUp() {
        // Testing #LANG-304. Must be called before availableLocaleSet is called.
        LocaleUtils.isAvailableLocale(Locale.getDefault());
    }

    @Test
    void testConstructor() {
        assertNotNull(new LocaleUtils());
        
        final Constructor<?>[] constructors = LocaleUtils.class.getDeclaredConstructors();
        assertEquals(1, constructors.length);
        assertTrue(Modifier.isPublic(constructors[0].getModifiers()));
        assertTrue(Modifier.isPublic(LocaleUtils.class.getModifiers()));
        assertFalse(Modifier.isFinal(LocaleUtils.class.getModifiers()));
    }

    @Test
    void testAvailableLocaleList() {
        final List<Locale> localeList = LocaleUtils.availableLocaleList();
        final List<Locale> secondCall = LocaleUtils.availableLocaleList();
        
        assertNotNull(localeList);
        assertSame(localeList, secondCall, "Should return same instance for performance");
        assertCollectionIsUnmodifiable(localeList);

        // Verify it matches JDK's available locales
        final Locale[] jdkLocales = Locale.getAvailableLocales();
        final List<Locale> expectedLocales = Arrays.asList(
            ArraySorter.sort(jdkLocales, Comparator.comparing(Locale::toString))
        );
        assertEquals(expectedLocales, localeList);
    }

    @Test
    void testAvailableLocaleSet() {
        final Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
        final Set<Locale> secondCall = LocaleUtils.availableLocaleSet();
        
        assertNotNull(localeSet);
        assertSame(localeSet, secondCall, "Should return same instance for performance");
        assertCollectionIsUnmodifiable(localeSet);

        // Verify it matches JDK's available locales
        final Set<Locale> expectedLocales = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        assertEquals(expectedLocales, localeSet);
    }

    @Test
    void testIsAvailableLocale() {
        final Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        
        // Test various locales to ensure consistency with available locale set
        assertLocaleAvailabilityMatches(ENGLISH, availableLocales);
        assertLocaleAvailabilityMatches(ENGLISH_US, availableLocales);
        assertLocaleAvailabilityMatches(ENGLISH_US_WITH_VARIANT, availableLocales);
        assertLocaleAvailabilityMatches(FRENCH, availableLocales);
        assertLocaleAvailabilityMatches(FRENCH_CANADA, availableLocales);
        assertLocaleAvailabilityMatches(UNKNOWN_LANGUAGE, availableLocales);
        assertLocaleAvailabilityMatches(UNKNOWN_LANGUAGE_AND_COUNTRY, availableLocales);
    }

    @Test
    void testIsLanguageUndetermined() {
        final Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        
        // Known languages should not be undetermined
        assertLanguageIsDetermined(ENGLISH, availableLocales);
        assertLanguageIsDetermined(ENGLISH_US, availableLocales);
        assertLanguageIsDetermined(FRENCH, availableLocales);
        assertLanguageIsDetermined(FRENCH_CANADA, availableLocales);
        
        // Unknown/invalid languages should be undetermined
        assertLanguageIsUndetermined(ENGLISH_US_WITH_VARIANT, availableLocales);
        assertLanguageIsUndetermined(UNKNOWN_LANGUAGE, availableLocales);
        assertLanguageIsUndetermined(UNKNOWN_LANGUAGE_AND_COUNTRY, availableLocales);
        
        assertTrue(LocaleUtils.isLanguageUndetermined(null));
    }

    @Test
    void testCountriesByLanguage() {
        assertCountriesForLanguage(null, new String[0]);
        assertCountriesForLanguage("de", new String[]{"DE", "CH", "AT", "LU"});
        assertCountriesForLanguage("zz", new String[0]); // Non-existent language
        assertCountriesForLanguage("it", new String[]{"IT", "CH"});
    }

    @Test
    void testLanguagesByCountry() {
        assertLanguagesForCountry(null, new String[0]);
        assertLanguagesForCountry("GB", new String[]{"en"});
        assertLanguagesForCountry("ZZ", new String[0]); // Non-existent country
        assertLanguagesForCountry("CH", new String[]{"fr", "de", "it"});
    }

    @Test
    void testLocaleLookupList_WithoutDefaultLocale() {
        assertLocaleLookupList(null, null, new Locale[0]);
        assertLocaleLookupList(UNKNOWN_LANGUAGE, null, new Locale[]{UNKNOWN_LANGUAGE});
        assertLocaleLookupList(ENGLISH, null, new Locale[]{ENGLISH});
        assertLocaleLookupList(ENGLISH_US, null, new Locale[]{ENGLISH_US, ENGLISH});
        assertLocaleLookupList(ENGLISH_US_WITH_VARIANT, null, 
            new Locale[]{ENGLISH_US_WITH_VARIANT, ENGLISH_US, ENGLISH});
    }

    @Test
    void testLocaleLookupList_WithDefaultLocale() {
        // Same locale as default
        assertLocaleLookupList(UNKNOWN_LANGUAGE, UNKNOWN_LANGUAGE, new Locale[]{UNKNOWN_LANGUAGE});
        assertLocaleLookupList(ENGLISH, ENGLISH, new Locale[]{ENGLISH});

        // Different default locale
        assertLocaleLookupList(ENGLISH_US, UNKNOWN_LANGUAGE,
            new Locale[]{ENGLISH_US, ENGLISH, UNKNOWN_LANGUAGE});
        assertLocaleLookupList(ENGLISH_US, UNKNOWN_LANGUAGE_AND_COUNTRY,
            new Locale[]{ENGLISH_US, ENGLISH, UNKNOWN_LANGUAGE_AND_COUNTRY});

        // Complex locale with variant
        assertLocaleLookupList(ENGLISH_US_WITH_VARIANT, UNKNOWN_LANGUAGE,
            new Locale[]{ENGLISH_US_WITH_VARIANT, ENGLISH_US, ENGLISH, UNKNOWN_LANGUAGE});
        assertLocaleLookupList(FRENCH_CANADA, ENGLISH,
            new Locale[]{FRENCH_CANADA, FRENCH, ENGLISH});
    }

    @Test
    void testToLocale_SingleLanguageCode() {
        assertNull(LocaleUtils.toLocale((String) null));
        
        // Valid single language codes
        assertValidSingleLanguageLocale("us");
        assertValidSingleLanguageLocale("fr");
        assertValidSingleLanguageLocale("de");
        assertValidSingleLanguageLocale("zh");
        assertValidSingleLanguageLocale("qq"); // Valid format but non-existent language
        assertValidSingleLanguageLocale(""); // Empty locale (JDK 8+)

        // Invalid formats
        assertInvalidLocaleString("Us", "Should fail if not lowercase");
        assertInvalidLocaleString("uS", "Should fail if not lowercase");
        assertInvalidLocaleString("u#", "Should fail if not lowercase");
        assertInvalidLocaleString("u", "Must be 2 chars if less than 5");
        assertInvalidLocaleString("uu_U", "Must be 2 chars if less than 5");
    }

    @Test
    void testToLocale_LanguageAndCountry() {
        assertValidLanguageCountryLocale("us_EN", "us", "EN");
        assertValidLanguageCountryLocale("us-EN", "us", "EN"); // Dash separator
        assertValidLanguageCountryLocale("us_ZH", "us", "ZH"); // Valid format but non-existent

        // Invalid formats
        assertInvalidLocaleString("us_En", "Should fail second part not uppercase");
        assertInvalidLocaleString("us_en", "Should fail second part not uppercase");
        assertInvalidLocaleString("us_eN", "Should fail second part not uppercase");
        assertInvalidLocaleString("uS_EN", "Should fail first part not lowercase");
        assertInvalidLocaleString("us_E3", "Should fail second part not uppercase");
    }

    @Test
    void testToLocale_LanguageCountryAndVariant() {
        assertValidLanguageCountryVariantLocale("us_EN_A", "us", "EN", "A");
        assertValidLanguageCountryVariantLocale("us-EN-A", "us", "EN", "A"); // Dash separator
        
        // JDK version-dependent behavior for variant case
        if (SystemUtils.isJavaVersionAtLeast(JAVA_1_4)) {
            assertValidLanguageCountryVariantLocale("us_EN_a", "us", "EN", "a");
            assertValidLanguageCountryVariantLocale("us_EN_SFsafdFDsdfF", "us", "EN", "SFsafdFDsdfF");
        } else {
            assertValidLanguageCountryVariantLocale("us_EN_a", "us", "EN", "A");
            assertValidLanguageCountryVariantLocale("us_EN_SFsafdFDsdfF", "us", "EN", "SFSAFDFDSDFF");
        }
        
        // Invalid formats
        assertInvalidLocaleString("us_EN-a", "Should fail as no consistent delimiter");
        assertInvalidLocaleString("uu_UU_", "Must be 3, 5 or 7+ in length");
        
        // LANG-1741: Complex variant
        assertEquals(new Locale("en", "001", "US_POSIX"), LocaleUtils.toLocale("en_001_US_POSIX"));
    }

    @Test
    void testToLocale_LocaleParameter() {
        assertNull(LocaleUtils.toLocale((String) null));
        assertEquals(Locale.getDefault(), LocaleUtils.toLocale((Locale) null));
        assertEquals(Locale.getDefault(), LocaleUtils.toLocale(Locale.getDefault()));
    }

    @Test
    void testThreeCharacterLanguageCodes() {
        // Test for LANG-915: Support for 3-character language codes
        for (final String languageCode : Arrays.asList("udm", "tet")) {
            final Locale locale = LocaleUtils.toLocale(languageCode);
            assertNotNull(locale);
            assertEquals(languageCode, locale.getLanguage());
            assertTrue(StringUtils.isBlank(locale.getCountry()));
            assertEquals(new Locale(languageCode), locale);
        }
    }

    @Test
    void testLanguageOnlyVariant_Lang328() {
        // Tests #LANG-328 - only language+variant (no country)
        assertValidLanguageCountryVariantLocale("fr__P", "fr", "", "P");
        assertValidLanguageCountryVariantLocale("fr__POSIX", "fr", "", "POSIX");
    }

    @Test
    void testUnderscorePrefix_Lang865() {
        // Tests #LANG-865 - strings starting with underscore
        assertValidLanguageCountryVariantLocale("_GB", "", "GB", "");
        assertValidLanguageCountryVariantLocale("_GB_P", "", "GB", "P");
        assertValidLanguageCountryVariantLocale("_GB_POSIX", "", "GB", "POSIX");
        
        // Invalid underscore-prefixed formats
        assertInvalidLocaleString("_G", "Must be at least 3 chars if starts with underscore");
        assertInvalidLocaleString("_Gb", "Must be uppercase if starts with underscore");
        assertInvalidLocaleString("_gB", "Must be uppercase if starts with underscore");
        assertInvalidLocaleString("_1B", "Must be letter if starts with underscore");
        assertInvalidLocaleString("_G1", "Must be letter if starts with underscore");
        assertInvalidLocaleString("_GB_", "Must be at least 5 chars if starts with underscore");
        assertInvalidLocaleString("_GBAP", 
            "Must have underscore after the country if starts with underscore and is at least 5 chars");
    }

    @Test
    void testNumericAreaCodes_Lang1312() {
        // Test UN M.49 numeric area codes
        assertValidLanguageCountryLocale("en_001", "en", "001");
        assertValidLanguageCountryLocale("en_150", "en", "150");
        assertValidLanguageCountryLocale("ar_001", "ar", "001");

        // LANG-1312: With variants
        assertValidLanguageCountryVariantLocale("en_001_GB", "en", "001", "GB");
        assertValidLanguageCountryVariantLocale("en_150_US", "en", "150", "US");
    }

    @ParameterizedTest
    @MethodSource("java.util.Locale#getAvailableLocales")
    void testToLocale_AllAvailableLocales(final Locale actualLocale) {
        assertEquals(actualLocale, LocaleUtils.toLocale(actualLocale));
    }

    @ParameterizedTest
    @MethodSource("java.util.Locale#getAvailableLocales")
    void testParseAllAvailableLocales(final Locale actualLocale) {
        // Check if it's possible to recreate the Locale using standard constructor
        final Locale reconstructedLocale = new Locale(
            actualLocale.getLanguage(), 
            actualLocale.getCountry(), 
            actualLocale.getVariant()
        );
        
        if (actualLocale.equals(reconstructedLocale)) {
            final String localeString = actualLocale.toString();
            
            // Look for script/extension suffix
            int suffixIndex = localeString.indexOf("_#");
            if (suffixIndex == -1) {
                suffixIndex = localeString.indexOf("#");
            }
            
            String parseableString = localeString;
            if (suffixIndex >= 0) {
                // Locale has suffix - should fail to parse
                assertInvalidLocaleString(localeString, "Locales with extensions should fail");
                // Try without suffix
                parseableString = localeString.substring(0, suffixIndex);
            }
            
            final Locale parsedLocale = LocaleUtils.toLocale(parseableString);
            assertEquals(actualLocale, parsedLocale);
        }
    }

    // Helper methods for better readability and reduced duplication

    private void assertCollectionIsUnmodifiable(final Collection<?> collection) {
        assertThrows(UnsupportedOperationException.class, () -> collection.add(null),
            "Collection should be unmodifiable");
    }

    private void assertLocaleAvailabilityMatches(final Locale locale, final Set<Locale> availableLocales) {
        assertEquals(availableLocales.contains(locale), LocaleUtils.isAvailableLocale(locale),
            "isAvailableLocale should match availableLocaleSet for " + locale);
    }

    private void assertLanguageIsDetermined(final Locale locale, final Set<Locale> availableLocales) {
        assertNotEquals(availableLocales.contains(locale), LocaleUtils.isLanguageUndetermined(locale),
            "Known language should be determined: " + locale);
    }

    private void assertLanguageIsUndetermined(final Locale locale, final Set<Locale> availableLocales) {
        assertEquals(availableLocales.contains(locale), LocaleUtils.isLanguageUndetermined(locale),
            "Unknown language should be undetermined: " + locale);
    }

    private void assertCountriesForLanguage(final String language, final String[] expectedCountries) {
        final List<Locale> localeList = LocaleUtils.countriesByLanguage(language);
        final List<Locale> secondCall = LocaleUtils.countriesByLanguage(language);
        
        assertNotNull(localeList);
        assertSame(localeList, secondCall, "Should return same instance for performance");
        
        // Verify all expected countries are present
        for (final String expectedCountry : expectedCountries) {
            boolean found = localeList.stream()
                .anyMatch(locale -> {
                    assertTrue(StringUtils.isEmpty(locale.getVariant()), 
                        "Should have empty variant: " + locale);
                    assertEquals(language, locale.getLanguage(), 
                        "Should have correct language: " + locale);
                    return expectedCountry.equals(locale.getCountry());
                });
            assertTrue(found, "Could not find country: " + expectedCountry + " for language: " + language);
        }
        
        assertCollectionIsUnmodifiable(localeList);
    }

    private void assertLanguagesForCountry(final String country, final String[] expectedLanguages) {
        final List<Locale> localeList = LocaleUtils.languagesByCountry(country);
        final List<Locale> secondCall = LocaleUtils.languagesByCountry(country);
        
        assertNotNull(localeList);
        assertSame(localeList, secondCall, "Should return same instance for performance");
        
        // Verify all expected languages are present
        for (final String expectedLanguage : expectedLanguages) {
            boolean found = localeList.stream()
                .anyMatch(locale -> {
                    assertTrue(StringUtils.isEmpty(locale.getVariant()), 
                        "Should have empty variant: " + locale);
                    assertEquals(country, locale.getCountry(), 
                        "Should have correct country: " + locale);
                    return expectedLanguage.equals(locale.getLanguage());
                });
            assertTrue(found, "Could not find language: " + expectedLanguage + " for country: " + country);
        }
        
        assertCollectionIsUnmodifiable(localeList);
    }

    private void assertLocaleLookupList(final Locale locale, final Locale defaultLocale, 
                                       final Locale[] expectedLocales) {
        final List<Locale> actualList = defaultLocale == null ?
                LocaleUtils.localeLookupList(locale) :
                LocaleUtils.localeLookupList(locale, defaultLocale);

        assertEquals(expectedLocales.length, actualList.size());
        assertEquals(Arrays.asList(expectedLocales), actualList);
        assertCollectionIsUnmodifiable(actualList);
    }

    private void assertValidSingleLanguageLocale(final String languageCode) {
        final Locale locale = LocaleUtils.toLocale(languageCode);
        assertNotNull(locale, "Should create valid locale for: " + languageCode);
        assertEquals(languageCode, locale.getLanguage());
        assertTrue(StringUtils.isEmpty(locale.getCountry()), "Country should be empty");
        assertTrue(StringUtils.isEmpty(locale.getVariant()), "Variant should be empty");
    }

    private void assertValidLanguageCountryLocale(final String localeString, 
                                                 final String expectedLanguage, 
                                                 final String expectedCountry) {
        final Locale locale = LocaleUtils.toLocale(localeString);
        assertNotNull(locale, "Should create valid locale for: " + localeString);
        assertEquals(expectedLanguage, locale.getLanguage());
        assertEquals(expectedCountry, locale.getCountry());
        assertTrue(StringUtils.isEmpty(locale.getVariant()), "Variant should be empty");
    }

    private void assertValidLanguageCountryVariantLocale(final String localeString,
                                                        final String expectedLanguage,
                                                        final String expectedCountry,
                                                        final String expectedVariant) {
        final Locale locale = LocaleUtils.toLocale(localeString);
        assertNotNull(locale, "Should create valid locale for: " + localeString);
        assertEquals(expectedLanguage, locale.getLanguage());
        assertEquals(expectedCountry, locale.getCountry());
        assertEquals(expectedVariant, locale.getVariant());
    }

    private void assertInvalidLocaleString(final String localeString, final String reason) {
        assertIllegalArgumentException(() -> LocaleUtils.toLocale(localeString), reason);
    }
}