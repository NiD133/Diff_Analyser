package org.apache.commons.lang3;

import static org.apache.commons.lang3.LangAssertions.assertIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link org.apache.commons.lang3.LocaleUtils}.
 *
 * <p>This version has been refactored for clarity by removing unused helper methods and constants,
 * improving test names, and clarifying the logic within each test.</p>
 */
@DisplayName("Tests for org.apache.commons.lang3.LocaleUtils")
public class LocaleUtilsTest extends AbstractLangTest {

    /**
     * The original test file contained many constants and helper methods that were not used by any
     * of the tests in this file. They have been removed to improve clarity and focus.
     * The single used helper, assertUnmodifiableCollection, is retained.
     */

    /**
     * Asserts that the given collection is unmodifiable.
     *
     * @param coll the collection to check
     */
    private static void assertUnmodifiableCollection(final Collection<?> coll) {
        assertThrows(UnsupportedOperationException.class, () -> coll.add(null),
            "Collection should be unmodifiable");
    }

    @BeforeEach
    public void setUp() {
        // Per LANG-304, this call is needed to initialize the locale set
        // before other methods in LocaleUtils are tested.
        LocaleUtils.isAvailableLocale(Locale.getDefault());
    }

    @DisplayName("toLocale(String) should correctly parse string representations of available locales")
    @ParameterizedTest(name = "Locale: {0}")
    @MethodSource("java.util.Locale#getAvailableLocales")
    void toLocaleFromString_shouldParseAvailableLocales(final Locale systemLocale) {
        // This test focuses on locales representable by new Locale(lang, country, variant).
        // Locales with scripts or extensions (e.g., sr-Latn-RS) are filtered out by this check,
        // as they cannot be fully reconstructed with that constructor.
        final Locale simpleLocale = new Locale(systemLocale.getLanguage(), systemLocale.getCountry(), systemLocale.getVariant());
        if (!systemLocale.equals(simpleLocale)) {
            return;
        }

        final String localeString = systemLocale.toString();

        // The toLocale() method does not support locale strings with '#' characters,
        // which are used for private use extensions or to denote variants in some contexts.
        if (localeString.contains("#")) {
            // It is expected to throw an exception for such strings.
            assertIllegalArgumentException(() -> LocaleUtils.toLocale(localeString),
                "toLocale() should fail for locale string with '#': " + localeString);
        } else {
            // For standard locale strings, toLocale() should parse them back to an equal Locale object.
            final Locale parsedLocale = LocaleUtils.toLocale(localeString);
            assertEquals(systemLocale, parsedLocale,
                "Parsed locale should be equal to the original for string: " + localeString);
        }
    }

    @DisplayName("toLocale(Locale) should return the provided locale")
    @ParameterizedTest(name = "Locale: {0}")
    @MethodSource("java.util.Locale#getAvailableLocales")
    void toLocaleFromLocale_shouldReturnSameLocale(final Locale systemLocale) {
        assertEquals(systemLocale, LocaleUtils.toLocale(systemLocale));
    }

    @Test
    @DisplayName("availableLocaleList() should return an unmodifiable, sorted list of all available locales")
    void availableLocaleList_shouldReturnUnmodifiableSortedListOfAllAvailableLocales() {
        // Act
        final List<Locale> actualList = LocaleUtils.availableLocaleList();

        // Assert: The list should be cached; subsequent calls return the same instance.
        final List<Locale> sameList = LocaleUtils.availableLocaleList();
        assertSame(actualList, sameList, "availableLocaleList() should return a cached instance");

        // Assert: The list should be unmodifiable.
        assertUnmodifiableCollection(actualList);

        // Assert: The list content should match the sorted list of all available JDK locales.
        final List<Locale> expectedList = Arrays.stream(Locale.getAvailableLocales())
                                                .sorted(Comparator.comparing(Locale::toString))
                                                .collect(Collectors.toList());
        assertEquals(expectedList, actualList, "List content should match sorted JDK available locales");
    }
}