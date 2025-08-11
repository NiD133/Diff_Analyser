package org.apache.commons.lang3;

import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Readable, intention-revealing tests for LocaleUtils.
 *
 * These tests group behavior by feature and avoid opaque inputs.
 * We do not assert system-dependent counts (which may vary by JRE),
 * instead we verify structural properties and invariants.
 */
public class LocaleUtilsTest {

    // ----------------------------------------------------------------------
    // toLocale(String)
    // ----------------------------------------------------------------------

    @Test
    public void toLocale_nullString_returnsNull() {
        assertNull(LocaleUtils.toLocale((String) null));
    }

    @Test
    public void toLocale_emptyString_returnsRootLocale() {
        Locale locale = LocaleUtils.toLocale("");
        assertEquals(Locale.ROOT, locale);
        assertEquals("", locale.getLanguage());
        assertEquals("", locale.getCountry());
        assertEquals("", locale.getVariant());
    }

    @Test
    public void toLocale_languageOnly_lowercase() {
        Locale locale = LocaleUtils.toLocale("fr");
        assertEquals("fr", locale.getLanguage());
        assertEquals("", locale.getCountry());
        assertEquals("", locale.getVariant());
    }

    @Test
    public void toLocale_languageAndCountry_acceptsUnderscoreAndDash() {
        Locale underscore = LocaleUtils.toLocale("zh_CN");
        Locale dash = LocaleUtils.toLocale("zh-CN");

        assertEquals("zh", underscore.getLanguage());
        assertEquals("CN", underscore.getCountry());
        assertEquals(underscore, dash);
    }

    @Test
    public void toLocale_languageCountryVariant() {
        Locale locale = LocaleUtils.toLocale("fr_CA_xxx");
        assertEquals("fr", locale.getLanguage());
        assertEquals("CA", locale.getCountry());
        assertEquals("xxx", locale.getVariant());
    }

    @Test
    public void toLocale_supportsNumericAreaCodes() {
        Locale locale = LocaleUtils.toLocale("en_001");
        assertEquals("en", locale.getLanguage());
        assertEquals("001", locale.getCountry());
    }

    @Test
    public void toLocale_invalid_wrongCountryCase_throws() {
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_gb"));
    }

    @Test
    public void toLocale_invalid_badSeparator_throws() {
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("fr--CA"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("fr__CA"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("fr-"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("_CA")); // Missing language
    }

    @Test
    public void toLocale_invalid_unrecognizedFormat_throws() {
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("#"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("+"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("zh-XCN")); // wrong length for country
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("zh-uCN")); // wrong case for country
    }

    // ----------------------------------------------------------------------
    // toLocale(Locale)
    // ----------------------------------------------------------------------

    @Test
    public void toLocale_LocaleOverload_returnsSameInstanceWhenNonNull() {
        Locale input = Locale.ITALIAN;
        Locale result = LocaleUtils.toLocale(input);
        assertSame(input, result);
    }

    @Test
    public void toLocale_LocaleOverload_returnsDefaultWhenNull() {
        Locale result = LocaleUtils.toLocale((Locale) null);
        assertSame(Locale.getDefault(), result);
    }

    // ----------------------------------------------------------------------
    // localeLookupList
    // ----------------------------------------------------------------------

    @Test
    public void localeLookupList_withoutDefault_buildsSpecificToGeneral() {
        Locale input = new Locale("fr", "CA", "POSIX");
        List<Locale> lookup = LocaleUtils.localeLookupList(input);

        assertEquals(3, lookup.size());
        assertEquals(new Locale("fr", "CA", "POSIX"), lookup.get(0));
        assertEquals(new Locale("fr", "CA"), lookup.get(1));
        assertEquals(new Locale("fr"), lookup.get(2));
    }

    @Test
    public void localeLookupList_withDefault_appendsDefaultIfNotDuplicate() {
        Locale input = new Locale("fr", "CA", "POSIX");
        Locale def = Locale.ENGLISH;

        List<Locale> lookup = LocaleUtils.localeLookupList(input, def);

        assertEquals(4, lookup.size());
        assertEquals(new Locale("fr", "CA", "POSIX"), lookup.get(0));
        assertEquals(new Locale("fr", "CA"), lookup.get(1));
        assertEquals(new Locale("fr"), lookup.get(2));
        assertEquals(def, lookup.get(3));
    }

    @Test
    public void localeLookupList_nullInputs_returnEmptyOrDefaultOnly() {
        assertTrue(LocaleUtils.localeLookupList((Locale) null).isEmpty());

        Locale def = Locale.ENGLISH;
        List<Locale> lookup = LocaleUtils.localeLookupList(null, def);
        assertEquals(1, lookup.size());
        assertEquals(def, lookup.get(0));
    }

    // ----------------------------------------------------------------------
    // availableLocaleList / availableLocaleSet
    // ----------------------------------------------------------------------

    @Test
    public void availableLocales_nonEmpty_unmodifiable_andConsistent() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        Set<Locale> set = LocaleUtils.availableLocaleSet();

        assertFalse("List should not be empty", list.isEmpty());
        assertFalse("Set should not be empty", set.isEmpty());

        // Unmodifiable
        assertThrows(UnsupportedOperationException.class, () -> list.add(Locale.ROOT));
        assertThrows(UnsupportedOperationException.class, () -> set.add(Locale.ROOT));

        // Same elements
        assertEquals(new HashSet<>(list), set);
        assertTrue(set.containsAll(list));
    }

    // ----------------------------------------------------------------------
    // languagesByCountry / countriesByLanguage
    // ----------------------------------------------------------------------

    @Test
    public void languagesByCountry_null_returnsEmpty() {
        assertTrue(LocaleUtils.languagesByCountry(null).isEmpty());
    }

    @Test
    public void languagesByCountry_filtersByCountryAndNoVariant() {
        List<Locale> usLanguages = LocaleUtils.languagesByCountry("US");
        for (Locale l : usLanguages) {
            assertEquals("US", l.getCountry());
            assertTrue("Expected no variant", l.getVariant().isEmpty());
        }
    }

    @Test
    public void countriesByLanguage_null_returnsEmpty() {
        assertTrue(LocaleUtils.countriesByLanguage(null).isEmpty());
    }

    @Test
    public void countriesByLanguage_filtersByLanguageAndNoVariant() {
        List<Locale> englishCountries = LocaleUtils.countriesByLanguage("en");
        for (Locale l : englishCountries) {
            assertEquals("en", l.getLanguage());
            assertTrue("Expected no variant", l.getVariant().isEmpty());
        }
    }

    // ----------------------------------------------------------------------
    // isAvailableLocale
    // ----------------------------------------------------------------------

    @Test
    public void isAvailableLocale_trueForStandardLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ITALY));
    }

    @Test
    public void isAvailableLocale_falseForNull() {
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    // ----------------------------------------------------------------------
    // isLanguageUndetermined
    // ----------------------------------------------------------------------

    @Test
    public void isLanguageUndetermined_trueForNullAndRoot() {
        assertTrue(LocaleUtils.isLanguageUndetermined(null));
        assertTrue(LocaleUtils.isLanguageUndetermined(Locale.ROOT));
    }

    @Test
    public void isLanguageUndetermined_falseForKnownLanguage() {
        assertFalse(LocaleUtils.isLanguageUndetermined(Locale.GERMANY));
        assertFalse(LocaleUtils.isLanguageUndetermined(Locale.ENGLISH));
    }
}