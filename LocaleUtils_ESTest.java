package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.apache.commons.lang3.LocaleUtils;

/**
 * Test suite for LocaleUtils class functionality.
 * Tests locale parsing, validation, lookup operations, and utility methods.
 */
public class LocaleUtilsTest {

    // ========== Locale Parsing Tests ==========
    
    @Test
    public void toLocale_shouldReturnNullForNullInput() {
        Locale result = LocaleUtils.toLocale((String) null);
        assertNull("toLocale should return null for null input", result);
    }

    @Test
    public void toLocale_shouldReturnEmptyLocaleForEmptyString() {
        Locale result = LocaleUtils.toLocale("");
        assertEquals("Empty string should create locale with empty language", "", result.getLanguage());
    }

    @Test
    public void toLocale_shouldParseLanguageOnlyLocale() {
        Locale result = LocaleUtils.toLocale("fr");
        assertNotNull("Valid language code should create locale", result);
        assertEquals("Language should be parsed correctly", "fr", result.getLanguage());
    }

    @Test
    public void toLocale_shouldParseLanguageAndCountryWithUnderscore() {
        Locale result = LocaleUtils.toLocale("zh-CN");
        assertNotNull("Valid language-country format should create locale", result);
        assertEquals("Locale string should be formatted correctly", "zh_CN", result.toString());
    }

    @Test
    public void toLocale_shouldParseLanguageCountryAndVariant() {
        Locale result = LocaleUtils.toLocale("bfi-TN-D9");
        assertNotNull("Valid language-country-variant format should create locale", result);
        assertEquals("Full locale should be formatted correctly", "bfi_TN_D9", result.toString());
    }

    @Test
    public void toLocale_shouldParseNumericCountryCode() {
        Locale result = LocaleUtils.toLocale("biTN-009");
        assertEquals("Numeric country codes should be supported", "bitn_009", result.toString());
        assertNotNull("Locale with numeric country should be created", result);
    }

    @Test
    public void toLocale_shouldHandleSpecialDashFormat() {
        Locale result = LocaleUtils.toLocale("-DY-n");
        assertEquals("Special dash format should be parsed", "_DY_n", result.toString());
    }

    @Test
    public void toLocale_shouldHandleCountryOnlyFormat() {
        Locale result = LocaleUtils.toLocale("-CN");
        assertEquals("Country-only format should work", "CHN", result.getISO3Country());
    }

    // ========== Locale Parsing Error Cases ==========

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_shouldRejectInvalidFormat_mixedCaseCountry() {
        LocaleUtils.toLocale("zh-uCN");
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_shouldRejectInvalidFormat_invalidCountryCode() {
        LocaleUtils.toLocale("zh-XCN");
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_shouldRejectInvalidFormat_tooLongCountry() {
        LocaleUtils.toLocale("-CIIN");
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_shouldRejectInvalidFormat_tooShortCountry() {
        LocaleUtils.toLocale("-j");
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_shouldRejectInvalidFormat_specialCharactersInCountry() {
        LocaleUtils.toLocale("-C!N");
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_shouldRejectInvalidFormat_invalidLanguageStart() {
        LocaleUtils.toLocale("_HUP");
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_shouldRejectInvalidFormat_doubleHyphen() {
        LocaleUtils.toLocale("at--");
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_shouldRejectInvalidFormat_invalidVariantLength() {
        LocaleUtils.toLocale("bi-K-D9");
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_shouldRejectInvalidFormat_numericLanguage() {
        LocaleUtils.toLocale("uiTN-0029");
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_shouldRejectInvalidFormat_specialCharacters() {
        LocaleUtils.toLocale("+");
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_shouldRejectInvalidFormat_hashSymbol() {
        LocaleUtils.toLocale("#");
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_shouldRejectInvalidFormat_complexInvalidString() {
        LocaleUtils.toLocale("_9DSQCbSw^!e Mr4eg6");
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_shouldRejectInvalidFormat_unicodeCharacters() {
        LocaleUtils.toLocale("\u00EDslenska");
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_shouldRejectInvalidFormat_mixedInvalidCharacters() {
        LocaleUtils.toLocale(")g%EOd_,G_0^e~VZj");
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_shouldRejectInvalidFormat_invalidVariantCharacters() {
        LocaleUtils.toLocale("DX\"n{f!cA_1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLocale_shouldRejectInvalidFormat_specialCharactersInVariant() {
        LocaleUtils.toLocale("q_]:a$7mI");
    }

    // ========== Locale Conversion Tests ==========

    @Test
    public void toLocale_shouldReturnDefaultForNullLocale() {
        Locale result = LocaleUtils.toLocale((Locale) null);
        assertEquals("Null locale should return default with English", "eng", result.getISO3Language());
    }

    @Test
    public void toLocale_shouldReturnSameLocaleForValidInput() {
        Locale input = Locale.ITALIAN;
        Locale result = LocaleUtils.toLocale(input);
        assertEquals("Valid locale should be returned as-is", "ita", result.getISO3Language());
    }

    // ========== Locale Lookup Tests ==========

    @Test
    public void localeLookupList_shouldReturnEmptyForNullInput() {
        List<Locale> result = LocaleUtils.localeLookupList((Locale) null);
        assertEquals("Null locale should return empty list", 0, result.size());
    }

    @Test
    public void localeLookupList_shouldReturnEmptyForBothNullInputs() {
        List<Locale> result = LocaleUtils.localeLookupList((Locale) null, (Locale) null);
        assertTrue("Both null inputs should return empty list", result.isEmpty());
    }

    @Test
    public void localeLookupList_shouldReturnSingleItemWhenLocaleEqualsDefault() {
        Locale locale = Locale.ENGLISH;
        List<Locale> result = LocaleUtils.localeLookupList(locale, locale);
        assertEquals("Same locale and default should return single item", 1, result.size());
    }

    @Test
    public void localeLookupList_shouldIncludeDefaultLocale() {
        Locale locale = Locale.ENGLISH;
        Locale defaultLocale = Locale.ROOT;
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        
        assertEquals("Should return locale hierarchy plus default", 2, result.size());
        assertTrue("Should contain default locale", result.contains(defaultLocale));
    }

    @Test
    public void localeLookupList_shouldCreateHierarchy() {
        Locale primaryLocale = Locale.SIMPLIFIED_CHINESE;
        Locale fallbackLocale = new Locale("Italy", "und", "LV");
        List<Locale> result = LocaleUtils.localeLookupList(fallbackLocale, primaryLocale);
        
        assertEquals("Should create proper locale hierarchy", 4, result.size());
    }

    // ========== Language Determination Tests ==========

    @Test
    public void isLanguageUndetermined_shouldReturnTrueForNullLocale() {
        boolean result = LocaleUtils.isLanguageUndetermined((Locale) null);
        assertTrue("Null locale should be considered undetermined", result);
    }

    @Test
    public void isLanguageUndetermined_shouldReturnTrueForRootLocale() {
        boolean result = LocaleUtils.isLanguageUndetermined(Locale.ROOT);
        assertTrue("Root locale should be considered undetermined", result);
    }

    @Test
    public void isLanguageUndetermined_shouldReturnFalseForValidLanguage() {
        boolean result = LocaleUtils.isLanguageUndetermined(Locale.GERMANY);
        assertFalse("Valid language locale should not be undetermined", result);
    }

    // ========== Locale Availability Tests ==========

    @Test
    public void isAvailableLocale_shouldReturnFalseForNull() {
        boolean result = LocaleUtils.isAvailableLocale((Locale) null);
        assertFalse("Null locale should not be available", result);
    }

    @Test
    public void isAvailableLocale_shouldReturnTrueForKnownLocale() {
        boolean result = LocaleUtils.isAvailableLocale(Locale.ITALY);
        assertTrue("Standard locale should be available", result);
    }

    // ========== Locale Collection Tests ==========

    @Test
    public void availableLocaleList_shouldReturnNonEmptyList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertFalse("Available locale list should not be empty", result.isEmpty());
    }

    @Test
    public void availableLocaleSet_shouldReturnNonEmptySet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertFalse("Available locale set should not be empty", result.isEmpty());
    }

    // ========== Language/Country Lookup Tests ==========

    @Test
    public void languagesByCountry_shouldReturnEmptyForNull() {
        List<Locale> result = LocaleUtils.languagesByCountry((String) null);
        assertTrue("Null country should return empty list", result.isEmpty());
    }

    @Test
    public void languagesByCountry_shouldReturnLanguagesForValidCountry() {
        List<Locale> result = LocaleUtils.languagesByCountry("RO");
        assertEquals("Romania should have languages", 1, result.size());
    }

    @Test
    public void countriesByLanguage_shouldReturnEmptyForNull() {
        List<Locale> result = LocaleUtils.countriesByLanguage((String) null);
        assertTrue("Null language should return empty list", result.isEmpty());
    }

    @Test
    public void countriesByLanguage_shouldReturnEmptyForInvalidLanguage() {
        List<Locale> result = LocaleUtils.countriesByLanguage("q@:a$7mI");
        assertEquals("Invalid language should return empty list", 0, result.size());
    }

    // ========== Constructor Test ==========

    @Test
    public void constructor_shouldAllowInstantiation() {
        // Test that constructor works (even though it's deprecated)
        LocaleUtils localeUtils = new LocaleUtils();
        assertNotNull("Constructor should create instance", localeUtils);
    }
}