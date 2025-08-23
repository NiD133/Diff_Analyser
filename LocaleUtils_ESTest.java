package org.apache.commons.lang3;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;

import java.util.List;
import java.util.Locale;
import java.util.Set;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class LocaleUtils_ESTest extends LocaleUtils_ESTest_scaffolding {

    private static final String INVALID_LOCALE_FORMAT = "Invalid locale format: ";

    @Test(timeout = 4000)
    public void testLocaleLookupListWithSpecificAndDefaultLocale() {
        Locale specificLocale = new Locale("Italy", "und", "LV");
        List<Locale> locales = LocaleUtils.localeLookupList(specificLocale, Locale.SIMPLIFIED_CHINESE);
        assertEquals(4, locales.size());
    }

    @Test(timeout = 4000)
    public void testToLocaleWithInvalidFormatThrowsException() {
        assertInvalidLocaleFormat("uiTN-0029");
        assertInvalidLocaleFormat("\u00EDslenska");
        assertInvalidLocaleFormat("zh-XCN");
        assertInvalidLocaleFormat("_HUP");
        assertInvalidLocaleFormat("-CIIN");
        assertInvalidLocaleFormat("-j");
        assertInvalidLocaleFormat("-C!N");
        assertInvalidLocaleFormat("_9DSQCbSw^!e Mr4eg6");
        assertInvalidLocaleFormat("+");
        assertInvalidLocaleFormat("#");
        assertInvalidLocaleFormat("at--");
        assertInvalidLocaleFormat(")g%EOd_,G_0^e~VZj");
        assertInvalidLocaleFormat("zh-uCN");
        assertInvalidLocaleFormat("DX\"n{f!cA_1");
        assertInvalidLocaleFormat("q_]:a$7mI");
        assertInvalidLocaleFormat("bi-K-D9");
    }

    private void assertInvalidLocaleFormat(String localeString) {
        try {
            LocaleUtils.toLocale(localeString);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.lang3.LocaleUtils", e);
            assertTrue(e.getMessage().contains(INVALID_LOCALE_FORMAT + localeString));
        }
    }

    @Test(timeout = 4000)
    public void testLocaleLookupListWithNullLocales() {
        List<Locale> locales = LocaleUtils.localeLookupList(null, null);
        assertTrue(locales.isEmpty());
    }

    @Test(timeout = 4000)
    public void testToLocaleWithValidFormats() {
        Locale locale = LocaleUtils.toLocale("-DY-n");
        assertEquals("_DY_n", locale.toString());

        locale = LocaleUtils.toLocale("-CN");
        assertEquals("CHN", locale.getISO3Country());

        locale = LocaleUtils.toLocale("");
        assertEquals("", locale.getLanguage());

        locale = LocaleUtils.toLocale((String) null);
        assertNull(locale);

        locale = LocaleUtils.toLocale("zh-CN");
        assertNotNull(locale);
        assertEquals("zh_CN", locale.toString());

        locale = LocaleUtils.toLocale("fr");
        assertNotNull(locale);
        assertEquals("fr", locale.getLanguage());

        locale = LocaleUtils.toLocale("biTN-009");
        assertEquals("bitn_009", locale.toString());
        assertNotNull(locale);

        locale = LocaleUtils.toLocale("bfi-TN-D9");
        assertNotNull(locale);
        assertEquals("bfi_TN_D9", locale.toString());
    }

    @Test(timeout = 4000)
    public void testLocaleLookupListWithEnglishAndRootLocales() {
        Locale english = Locale.ENGLISH;
        Locale root = Locale.ROOT;
        List<Locale> locales = LocaleUtils.localeLookupList(english, root);
        assertEquals(2, locales.size());
        assertTrue(locales.contains(root));
    }

    @Test(timeout = 4000)
    public void testLocaleLookupListWithSameLocale() {
        Locale english = Locale.ENGLISH;
        List<Locale> locales = LocaleUtils.localeLookupList(english, english);
        assertEquals(1, locales.size());
    }

    @Test(timeout = 4000)
    public void testLanguagesByCountry() {
        List<Locale> locales = LocaleUtils.languagesByCountry(null);
        assertTrue(locales.isEmpty());

        locales = LocaleUtils.languagesByCountry("RO");
        assertEquals(1, locales.size());
    }

    @Test(timeout = 4000)
    public void testIsLanguageUndetermined() {
        assertTrue(LocaleUtils.isLanguageUndetermined(Locale.ROOT));
        assertFalse(LocaleUtils.isLanguageUndetermined(Locale.GERMANY));
        assertTrue(LocaleUtils.isLanguageUndetermined((Locale) null));
    }

    @Test(timeout = 4000)
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ITALY));
        assertFalse(LocaleUtils.isAvailableLocale((Locale) null));
    }

    @Test(timeout = 4000)
    public void testCountriesByLanguage() {
        List<Locale> locales = LocaleUtils.countriesByLanguage(null);
        assertTrue(locales.isEmpty());

        locales = LocaleUtils.countriesByLanguage("q@:a$7mI");
        assertEquals(0, locales.size());
    }

    @Test(timeout = 4000)
    public void testAvailableLocales() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        assertFalse(locales.isEmpty());

        Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
        assertFalse(localeSet.isEmpty());
    }

    @Test(timeout = 4000)
    public void testLocaleLookupListWithNullLocale() {
        List<Locale> locales = LocaleUtils.localeLookupList((Locale) null);
        assertEquals(0, locales.size());
    }

    @Test(timeout = 4000)
    public void testLocaleUtilsConstructor() {
        new LocaleUtils();
    }
}