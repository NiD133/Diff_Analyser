package org.apache.commons.lang3;

import static org.apache.commons.lang3.LangAssertions.assertIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link LocaleUtils}.
 */
public class LocaleUtilsTest extends AbstractLangTest {

    /**
     * A simple helper to assert that a collection is unmodifiable.
     *
     * @param coll the collection to check
     */
    private static void assertUnmodifiableCollection(final Collection<?> coll) {
        assertThrows(UnsupportedOperationException.class, () -> coll.add(null),
            "Collection should be unmodifiable");
    }

    /**
     * The available locale set is cached in a static variable in {@link LocaleUtils}.
     * This setup method primes the cache before any tests are run to ensure that
     * test execution order does not affect outcomes.
     *
     * @see <a href="https://issues.apache.org/jira/browse/LANG-304">LANG-304</a>
     */
    @BeforeEach
    void primeLocaleUtilsCache() {
        LocaleUtils.isAvailableLocale(Locale.getDefault());
    }

    @Test
    void availableLocaleSet_shouldReturnCachedUnmodifiableSetOfAllAvailableLocales() {
        // Act
        final Set<Locale> set1 = LocaleUtils.availableLocaleSet();
        final Set<Locale> set2 = LocaleUtils.availableLocaleSet();

        // Assert
        assertNotNull(set1);
        assertSame(set1, set2, "The set should be cached and returned on subsequent calls.");

        final Set<Locale> expectedJdkSet = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        assertEquals(expectedJdkSet, set1, "The set should be equal to the one provided by the JDK.");

        assertUnmodifiableCollection(set1);
    }

    @ParameterizedTest
    @MethodSource("java.util.Locale#getAvailableLocales")
    void toLocale_withLocaleArgument_shouldReturnInput(final Locale locale) {
        // This test verifies the pass-through behavior of toLocale(Locale),
        // which should simply return the provided locale if it's not null.
        assertEquals(locale, LocaleUtils.toLocale(locale));
    }

    @ParameterizedTest
    @MethodSource("java.util.Locale#getAvailableLocales")
    void toLocale_shouldParseStringsOfAvailableLocales(final Locale availableLocale) {
        // This test aims to verify that LocaleUtils.toLocale(String) can correctly parse
        // the string representation of all available locales from the JDK.

        // The logic is complex because:
        // 1. LocaleUtils.toLocale(String) only supports "simple" locales (language, country, variant).
        //    It does not support locales with scripts or extensions (e.g., "sr-Latn-RS").
        // 2. The string representation (toString()) of some locales may contain characters (like '#')
        //    that LocaleUtils.toLocale(String) intentionally rejects.

        // Step 1: Filter for "simple" locales.
        // We identify them by checking if a locale can be fully recreated from its basic parts.
        // Locales with scripts or extensions will fail this check.
        final Locale reconstructedSimpleLocale = new Locale(
            availableLocale.getLanguage(),
            availableLocale.getCountry(),
            availableLocale.getVariant());

        if (!availableLocale.equals(reconstructedSimpleLocale)) {
            // This locale has script/extension data not captured by the basic constructor.
            // LocaleUtils.toLocale(String) is not expected to handle it, so we skip this test case.
            // Example: For Locale.forLanguageTag("sr-Latn-RS"), reconstructedSimpleLocale is "sr_RS",
            // which is not equal to the original.
            return;
        }

        // Step 2: Test parsing for the simple locale.
        final String localeString = availableLocale.toString();

        // Some locale toString() representations might include a script/extension-like suffix
        // (e.g., "th_TH_#u-nu-thai") which LocaleUtils.toLocale is designed to reject.
        int extensionSeparatorIndex = localeString.indexOf("_#");
        if (extensionSeparatorIndex == -1) {
            extensionSeparatorIndex = localeString.indexOf("#");
        }

        if (extensionSeparatorIndex >= 0) {
            // The string contains a suffix that toLocale(String) should reject.
            assertIllegalArgumentException(
                () -> LocaleUtils.toLocale(localeString),
                "toLocale() should fail for string with script/extension suffix: " + localeString);

            // However, it should succeed if we provide the string without the suffix.
            final String baseLocaleString = localeString.substring(0, extensionSeparatorIndex);
            final Locale parsedLocale = LocaleUtils.toLocale(baseLocaleString);

            // The original test asserted that the parsed locale from the base string should
            // equal the original locale. This implies that for locales reaching this path,
            // the suffix in toString() is metadata not affecting equality.
            assertEquals(availableLocale, parsedLocale);
        } else {
            // This is a standard locale string. toLocale() should parse it successfully.
            final Locale parsedLocale = LocaleUtils.toLocale(localeString);
            assertEquals(availableLocale, parsedLocale);
        }
    }
}