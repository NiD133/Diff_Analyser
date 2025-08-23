package org.apache.commons.lang3;

import static org.apache.commons.lang3.LangAssertions.assertIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link LocaleUtils}.
 *
 * This refactored version focuses on clarity and maintainability by:
 * - Using descriptive test method names and @DisplayName annotations.
 * - Removing unused helper methods and constants to reduce clutter.
 * - Adding comments and better variable names to clarify complex test logic.
 * - Structuring tests to clearly state their intent and expected outcomes.
 */
@DisplayName("Tests for LocaleUtils")
public class LocaleUtilsTest extends AbstractLangTest {

    @BeforeEach
    void setUp() {
        // This setup is required for a specific bug fix (LANG-304)
        // and must be called before the internal available locale set is accessed.
        LocaleUtils.isAvailableLocale(Locale.getDefault());
    }

    @Test
    @DisplayName("Constructor should be public and the class non-final for backwards compatibility")
    void constructor_shouldBePublicAndClassNonFinal() {
        // The constructor is deprecated and scheduled to become private, but this test
        // verifies its current public, non-final state for framework compatibility.
        assertNotNull(new LocaleUtils(), "Instantiation should be possible.");

        final Constructor<?>[] constructors = LocaleUtils.class.getDeclaredConstructors();
        assertEquals(1, constructors.length, "Should have exactly one constructor.");

        final Constructor<?> constructor = constructors[0];
        assertTrue(Modifier.isPublic(constructor.getModifiers()), "Constructor should be public.");
        assertTrue(Modifier.isPublic(LocaleUtils.class.getModifiers()), "Class should be public.");
        assertFalse(Modifier.isFinal(LocaleUtils.class.getModifiers()), "Class should not be final.");
    }

    @ParameterizedTest
    @MethodSource("java.util.Locale#getAvailableLocales")
    @DisplayName("toLocale(Locale) should return the input Locale instance")
    void toLocale_withLocale_shouldReturnSameLocale(final Locale locale) {
        assertEquals(locale, LocaleUtils.toLocale(locale));
    }

    @ParameterizedTest
    @MethodSource("java.util.Locale#getAvailableLocales")
    @DisplayName("toLocale(String) should correctly parse string representations of available locales")
    void toLocale_withStringFromAvailableLocale_shouldParseCorrectly(final Locale availableLocale) {
        // LocaleUtils.toLocale(String) is designed for locales constructed from language,
        // country, and variant. It does not support modern locales with scripts or extensions
        // (e.g., "ja_JP_JP" or "th_TH_TH_#u-nu-thai").
        // We first verify that the test locale can be represented in this simple form.
        final Locale simpleLocale = new Locale(availableLocale.getLanguage(), availableLocale.getCountry(), availableLocale.getVariant());
        if (!availableLocale.equals(simpleLocale)) {
            // If not, it's a locale with scripts/extensions. We skip testing it as it's
            // outside the scope of what toLocale(String) is designed to handle.
            return;
        }

        final String localeString = availableLocale.toString();
        String stringToParse = localeString;

        // BCP 47 extensions, marked by "_#" or "#", are not supported by toLocale(String).
        int suffixIndex = localeString.indexOf("_#");
        if (suffixIndex == -1) {
            suffixIndex = localeString.indexOf("#");
        }

        if (suffixIndex >= 0) {
            // The full string with an extension should be rejected with an IllegalArgumentException.
            assertIllegalArgumentException(() -> LocaleUtils.toLocale(localeString),
                "Should throw for locale string with an extension: " + localeString);

            // For the test, we proceed by parsing the string without the unsupported extension.
            stringToParse = localeString.substring(0, suffixIndex);
        }

        final Locale parsedLocale = LocaleUtils.toLocale(stringToParse);
        assertEquals(availableLocale, parsedLocale,
            () -> "Parsing '" + stringToParse + "' should result in the original locale " + availableLocale);
    }
}