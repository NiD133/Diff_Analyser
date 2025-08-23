package org.apache.commons.lang3;

import static org.apache.commons.lang3.JavaVersion.JAVA_1_4;
import static org.apache.commons.lang3.LangAssertions.assertIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link LocaleUtils}.
 */
class LocaleUtilsTest {

    @BeforeEach
    void setUp() {
        // This is needed for a bug fix in LANG-304.
        // It ensures that the available locale set is initialized before any tests that might rely on it.
        // Calling isAvailableLocale triggers the initialization of the static cache in LocaleUtils.
        LocaleUtils.isAvailableLocale(Locale.getDefault());
    }

    @Test
    @DisplayName("toLocale(null) should return null")
    void testToLocale_withNullInput_returnsNull() {
        // The original test suite was missing a direct test for this documented behavior.
        assertEquals(null, LocaleUtils.toLocale((String) null));
    }

    @Test
    @DisplayName("toLocale(Locale) should return the same locale instance")
    void testToLocale_withLocaleArgument_returnsSameLocale() {
        final Locale locale = new Locale("en", "US");
        assertEquals(locale, LocaleUtils.toLocale(locale));
    }

    @ParameterizedTest
    @MethodSource("java.util.Locale#getAvailableLocales")
    @DisplayName("toLocale(String) should correctly parse all available simple locales")
    void testToLocale_parsesAllAvailableLocales(final Locale actualLocale) {
        // A "simple" locale is one without script or extension tags,
        // which can be constructed via new Locale(lang, country, variant).
        final Locale simpleLocale = new Locale(actualLocale.getLanguage(), actualLocale.getCountry(), actualLocale.getVariant());
        final String localeString = actualLocale.toString();
        final boolean hasExtension = localeString.contains("#");

        // We only test locales that are "simple" and can be recreated by our method.
        // Locales with extensions are tested separately.
        if (actualLocale.equals(simpleLocale) && !hasExtension) {
            // Act
            final Locale parsedLocale = LocaleUtils.toLocale(localeString);
            // Assert
            assertEquals(actualLocale, parsedLocale);
        }
    }

    @Test
    @DisplayName("toLocale should throw an exception for strings with script/extension suffixes")
    void testToLocale_withScriptOrExtension_throwsIllegalArgumentException() {
        // The toLocale method does not support the # extension syntax from Java 7+
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("en_US_#Latn"));
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("en_US_#u-co-phonebk"));
    }

    @Test
    @DisplayName("toLocale should handle 3-part locale strings with valid delimiters")
    void testToLocale_withThreePartString_andValidDelimiters() {
        assertValidToLocale("us_EN_A", "us", "EN", "A");
        assertValidToLocale("us-EN-A", "us", "EN", "A");
    }

    @Test
    @DisplayName("toLocale should handle variant casing differently based on JDK version")
    void testToLocale_withThreePartString_handlesVariantCasingByJdkVersion() {
        // This test accounts for a known behavioral change in the JDK's Locale constructor.
        // See https://bugs.java.com/bugdatabase/view_bug.do?bug_id=4210525
        if (SystemUtils.isJavaVersionAtLeast(JAVA_1_4)) {
            assertValidToLocale("us_EN_a", "us", "EN", "a");
            assertValidToLocale("us_EN_SFsafdFDsdfF", "us", "EN", "SFsafdFDsdfF");
        } else {
            // In older JDKs, the variant was always upper-cased.
            assertValidToLocale("us_EN_a", "us", "EN", "A");
            assertValidToLocale("us_EN_SFsafdFDsdfF", "us", "EN", "SFSAFDFDSDFF");
        }
    }

    @Test
    @DisplayName("toLocale should throw an exception for 3-part strings with inconsistent delimiters")
    void testToLocale_withThreePartString_andInconsistentDelimiters_throwsException() {
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("us_EN-a"), "Should fail with inconsistent delimiters");
    }

    @Test
    @DisplayName("toLocale should throw an exception for malformed locale strings")
    void testToLocale_withMalformedString_throwsException() {
        assertIllegalArgumentException(() -> LocaleUtils.toLocale("uu_UU_"), "Must be 3, 5 or 7+ in length");
    }

    @Test
    @DisplayName("toLocale should handle numeric country codes and complex variants (LANG-1741)")
    void testToLocale_withNumericCountryAndComplexVariant_forLang1741() {
        final Locale expected = new Locale("en", "001", "US_POSIX");
        final Locale actual = LocaleUtils.toLocale("en_001_US_POSIX");
        assertEquals(expected, actual);
    }

    // --- Helper Methods ---

    /**
     * Asserts that a collection is unmodifiable by trying to add an element to it.
     */
    private static void assertUnmodifiableCollection(final List<?> coll) {
        assertThrows(UnsupportedOperationException.class, () -> coll.add(null));
    }

    /**
     * Asserts that {@link LocaleUtils#toLocale(String)} correctly parses a 1-part locale string.
     */
    private static void assertValidToLocale(final String language) {
        final Locale locale = LocaleUtils.toLocale(language);
        assertNotNull(locale, "valid locale");
        assertEquals(language, locale.getLanguage());
        assertTrue(StringUtils.isEmpty(locale.getCountry()), "Country should be empty");
        assertTrue(StringUtils.isEmpty(locale.getVariant()), "Variant should be empty");
    }

    /**
     * Asserts that {@link LocaleUtils#toLocale(String)} correctly parses a 2-part locale string.
     */
    private static void assertValidToLocale(final String localeString, final String language, final String country) {
        final Locale locale = LocaleUtils.toLocale(localeString);
        assertNotNull(locale, "valid locale");
        assertEquals(language, locale.getLanguage());
        assertEquals(country, locale.getCountry());
        assertTrue(StringUtils.isEmpty(locale.getVariant()), "Variant should be empty");
    }

    /**
     * Asserts that {@link LocaleUtils#toLocale(String)} correctly parses a 3-part locale string.
     */
    private static void assertValidToLocale(final String localeString, final String language, final String country, final String variant) {
        final Locale locale = LocaleUtils.toLocale(localeString);
        assertNotNull(locale, "valid locale");
        assertEquals(language, locale.getLanguage());
        assertEquals(country, locale.getCountry());
        assertEquals(variant, locale.getVariant());
    }
}