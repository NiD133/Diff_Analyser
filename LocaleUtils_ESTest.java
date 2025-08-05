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

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Unit tests for {@link LocaleUtils}.
 */
public class LocaleUtilsTest {

    //-----------------------------------------------------------------------
    // toLocale(String)
    //-----------------------------------------------------------------------

    @Test
    public void toLocale_shouldReturnNull_whenGivenNullString() {
        assertNull(LocaleUtils.toLocale((String) null));
    }

    @Test
    public void toLocale_shouldReturnRootLocale_whenGivenEmptyString() {
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(""));
    }

    @Test
    public void toLocale_shouldParseLanguageOnly() {
        assertEquals(new Locale("fr"), LocaleUtils.toLocale("fr"));
    }

    @Test
    public void toLocale_shouldParseLanguageAndCountry() {
        assertEquals(new Locale("zh", "CN"), LocaleUtils.toLocale("zh-CN"));
        assertEquals(new Locale("zh", "CN"), LocaleUtils.toLocale("zh_CN"));
    }

    @Test
    public void toLocale_shouldParseLanguageCountryAndVariant() {
        assertEquals(new Locale("bfi", "TN", "D9"), LocaleUtils.toLocale("bfi-TN-D9"));
        assertEquals(new Locale("bfi", "TN", "D9"), LocaleUtils.toLocale("bfi_TN_D9"));
    }

    @Test
    public void toLocale_shouldParseCountryAndVariantOnly() {
        // Format: _CC_VVV
        assertEquals(new Locale("", "DY", "n"), LocaleUtils.toLocale("_DY_n"));
        assertEquals(new Locale("", "DY", "n"), LocaleUtils.toLocale("-DY-n"));
    }

    @Test
    public void toLocale_shouldParseCountryOnly() {
        // Format: _CC
        assertEquals(new Locale("", "CN"), LocaleUtils.toLocale("_CN"));
        assertEquals(new Locale("", "CN"), LocaleUtils.toLocale("-CN"));
    }

    @Test
    public void toLocale_shouldParseLenientlyWithNonStandardCodes() {
        // The toLocale method is more lenient than the Javadoc suggests.
        // It does not strictly enforce ISO 639 for the language part in all cases.
        // Here, "biTN" is not a valid language code, but it's parsed successfully.
        Locale expected = new Locale("biTN", "009");
        Locale actual = LocaleUtils.toLocale("biTN-009");
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_shouldThrowException_forInvalidLanguageCode() {
        // Language code "uiTN" is invalid because it contains uppercase letters and is too long.
        LocaleUtils.toLocale("uiTN-0029");
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_shouldThrowException_forInvalidCountryCode() {
        // Country code "XCN" is invalid (not 2 letters or 3 digits).
        LocaleUtils.toLocale("zh-XCN");
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_shouldThrowException_forInvalidFormatWithTooManySeparators() {
        LocaleUtils.toLocale("at--");
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_shouldThrowException_forInvalidFormatWithShortCountryCode() {
        LocaleUtils.toLocale("-j");
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_shouldThrowException_forInvalidFormatWithLongCountryCode() {
        LocaleUtils.toLocale("-CIIN");
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_shouldThrowException_forInvalidCharacters() {
        LocaleUtils.toLocale("fr-C!N");
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_shouldThrowException_forShortAndInvalidString() {
        LocaleUtils.toLocale("+");
    }

    //-----------------------------------------------------------------------
    // toLocale(Locale)
    //-----------------------------------------------------------------------

    @Test
    public void toLocale_shouldReturnDefaultLocale_whenGivenNullLocale() {
        // This test is robust because it doesn't assume a specific default locale.
        assertEquals(Locale.getDefault(), LocaleUtils.toLocale((Locale) null));
    }

    @Test
    public void toLocale_shouldReturnSameLocale_whenGivenNonNullLocale() {
        assertEquals(Locale.ITALIAN, LocaleUtils.toLocale(Locale.ITALIAN));
    }

    //-----------------------------------------------------------------------
    // localeLookupList(locale, defaultLocale)
    //-----------------------------------------------------------------------

    @Test
    public void localeLookupList_shouldReturnCorrectList_forComplexLocale() {
        Locale locale = new Locale("fr", "CA", "VAR");
        List<Locale> expected = Arrays.asList(
                new Locale("fr", "CA", "VAR"),
                new Locale("fr", "CA"),
                new Locale("fr")
        );
        assertEquals(expected, LocaleUtils.localeLookupList(locale));
    }

    @Test
    public void localeLookupList_shouldReturnCorrectList_withDefaultLocale() {
        Locale locale = Locale.ENGLISH; // "en"
        Locale defaultLocale = Locale.ROOT; // ""
        List<Locale> expected = Arrays.asList(locale, defaultLocale);
        assertEquals(expected, LocaleUtils.localeLookupList(locale, defaultLocale));
    }

    @Test
    public void localeLookupList_shouldNotContainDuplicates() {
        Locale locale = Locale.ENGLISH;
        List<Locale> expected = Arrays.asList(Locale.ENGLISH);
        assertEquals(expected, LocaleUtils.localeLookupList(locale, locale));
    }

    @Test
    public void localeLookupList_shouldReturnEmptyList_forNullInputs() {
        assertTrue(LocaleUtils.localeLookupList(null, null).isEmpty());
    }

    @Test
    public void localeLookupList_shouldReturnEmptyList_forSingleNullInput() {
        assertTrue(LocaleUtils.localeLookupList(null).isEmpty());
    }

    //-----------------------------------------------------------------------
    // isAvailableLocale(Locale)
    //-----------------------------------------------------------------------

    @Test
    public void isAvailableLocale_shouldReturnTrue_forAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    }

    @Test
    public void isAvailableLocale_shouldReturnFalse_forNullLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    //-----------------------------------------------------------------------
    // isLanguageUndetermined(Locale)
    //-----------------------------------------------------------------------

    @Test
    public void isLanguageUndetermined_shouldReturnTrue_forRootLocale() {
        assertTrue(LocaleUtils.isLanguageUndetermined(Locale.ROOT));
    }


    @Test
    public void isLanguageUndetermined_shouldReturnTrue_forNullLocale() {
        assertTrue(LocaleUtils.isLanguageUndetermined(null));
    }

    @Test
    public void isLanguageUndetermined_shouldReturnFalse_forDeterminedLanguage() {
        assertFalse(LocaleUtils.isLanguageUndetermined(Locale.GERMANY));
    }

    //-----------------------------------------------------------------------
    // languagesByCountry(String) & countriesByLanguage(String)
    //-----------------------------------------------------------------------

    @Test
    public void languagesByCountry_shouldReturnCorrectLanguages() {
        // Romania ("RO") has Romanian ("ro") as a language.
        List<Locale> locales = LocaleUtils.languagesByCountry("RO");
        assertTrue(locales.contains(new Locale("ro", "RO")));
    }

    @Test
    public void languagesByCountry_shouldReturnEmptyList_forNullCountryCode() {
        assertTrue(LocaleUtils.languagesByCountry(null).isEmpty());
    }

    @Test
    public void countriesByLanguage_shouldReturnEmptyList_forNullLanguageCode() {
        assertTrue(LocaleUtils.countriesByLanguage(null).isEmpty());
    }

    @Test
    public void countriesByLanguage_shouldReturnEmptyList_forInvalidLanguageCode() {
        assertTrue(LocaleUtils.countriesByLanguage("zz").isEmpty());
    }

    //-----------------------------------------------------------------------
    // availableLocaleList() & availableLocaleSet()
    //-----------------------------------------------------------------------

    @Test
    public void availableLocaleList_shouldReturnNonEmptyList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }

    @Test
    public void availableLocaleSet_shouldReturnNonEmptySet() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        assertNotNull(set);
        assertFalse(set.isEmpty());
    }

    //-----------------------------------------------------------------------
    // Constructor
    //-----------------------------------------------------------------------

    @Test
    public void constructor_shouldBePublicForTools() {
        // The constructor is public to allow instantiation by tools, though it is deprecated.
        assertNotNull(new LocaleUtils());
    }
}