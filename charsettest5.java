package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests the edge-case parsing behavior of the dash character ('-') in {@link CharSet}.
 * The dash is a special character used to define ranges (e.g., "a-z"), but it can
 * also be treated as a literal character depending on its position.
 */
@DisplayName("CharSet Dash ('-') Parsing Rules")
class CharSetDashParsingTest extends AbstractLangTest {

    /**
     * Asserts that creating a CharSet from the given string results in the expected set of CharRanges.
     * This helper method simplifies tests by encapsulating the common Arrange-Act-Assert pattern.
     *
     * @param setStr         The string definition to pass to {@link CharSet#getInstance(String...)}.
     * @param expectedRanges The expected {@link CharRange} objects in the resulting set.
     */
    private void assertCharSetHasRanges(final String setStr, final CharRange... expectedRanges) {
        // Act: Create the CharSet from the input string.
        final CharSet charSet = CharSet.getInstance(setStr);
        final CharRange[] actualRanges = charSet.getCharRanges();

        // Assert: Verify the actual ranges match the expected ranges, ignoring order.
        final Set<CharRange> actualSet = new HashSet<>(Arrays.asList(actualRanges));
        final Set<CharRange> expectedSet = new HashSet<>(Arrays.asList(expectedRanges));

        assertEquals(expectedSet, actualSet, () -> "CharSet for \"" + setStr + "\" did not produce the expected ranges.");
    }

    @Nested
    @DisplayName("When dash is a literal character")
    class LiteralDashTests {

        @DisplayName("should parse strings with only dashes as a set containing a single literal dash")
        @ParameterizedTest(name = "Input: \"{0}\"")
        @ValueSource(strings = {"-", "--", "---", "----"})
        void getInstanceWithOnlyDashes(final String dashInput) {
            // A single dash, or multiple consecutive dashes, are parsed as a single
            // character set containing just the literal dash.
            assertCharSetHasRanges(dashInput, CharRange.is('-'));
        }

        @Test
        @DisplayName("should parse a dash at the start of a string as a literal")
        void getInstanceWithDashAtStart() {
            // A dash at the beginning of a set string is treated as a literal character,
            // not as part of a range.
            assertCharSetHasRanges("-a", CharRange.is('-'), CharRange.is('a'));
        }

        @Test
        @DisplayName("should parse a dash at the end of a string as a literal")
        void getInstanceWithDashAtEnd() {
            // A dash at the end of a set string is also treated as a literal character.
            assertCharSetHasRanges("a-", CharRange.is('a'), CharRange.is('-'));
        }
    }

    @Nested
    @DisplayName("When dash defines a character range")
    class DashAsRangeSpecifierTests {

        @Test
        @DisplayName("should parse 'a--' as a range from 'a' to '-'")
        void getInstanceWithDashAsRangeEnd() {
            // The parser sees 'a', then '-', then another character ('-').
            // This forms a valid range from character 'a' to character '-'.
            assertCharSetHasRanges("a--", CharRange.isIn('a', '-'));
        }

        @Test
        @DisplayName("should parse '--a' as a range from '-' to 'a'")
        void getInstanceWithDashAsRangeStart() {
            // The parser sees '-', then '-', then 'a'.
            // This forms a valid range from character '-' to character 'a'.
            assertCharSetHasRanges("--a", CharRange.isIn('-', 'a'));
        }
    }
}