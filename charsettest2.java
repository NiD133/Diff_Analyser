package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("CharSet Factory Tests")
public class CharSetTestTest2 extends AbstractLangTest {

    @Nested
    @DisplayName("getInstance(String) with mixed characters and ranges")
    class GetInstanceWithCombinations {

        /**
         * Asserts that creating a CharSet from a given string definition results in the expected set of CharRange objects.
         * This helper makes the tests more concise and readable.
         *
         * @param setDefinition  The string definition to pass to CharSet.getInstance().
         * @param expectedRanges The variable-length array of CharRange objects that are expected to be created.
         */
        private void assertCharSetContainsExactly(final String setDefinition, final CharRange... expectedRanges) {
            final CharSet charSet = CharSet.getInstance(setDefinition);
            final CharRange[] actualRanges = charSet.getCharRanges();

            // Convert both expected and actual ranges to sets for an order-independent comparison.
            // This is more robust than checking for the presence of each element individually.
            final Set<CharRange> expectedSet = new HashSet<>(Arrays.asList(expectedRanges));
            final Set<CharRange> actualSet = new HashSet<>(Arrays.asList(actualRanges));

            assertEquals(expectedSet, actualSet,
                () -> "CharSet for \"" + setDefinition + "\" did not produce the expected ranges.");
        }

        @Test
        @DisplayName("should parse a string of only single characters")
        void shouldParseStringOfSingleCharacters() {
            assertCharSetContainsExactly("abc",
                CharRange.is('a'),
                CharRange.is('b'),
                CharRange.is('c')
            );
        }

        @Test
        @DisplayName("should parse a string of multiple character ranges")
        void shouldParseStringOfMultipleRanges() {
            assertCharSetContainsExactly("a-ce-f",
                CharRange.isIn('a', 'c'),
                CharRange.isIn('e', 'f')
            );
        }

        @Test
        @DisplayName("should parse a mix of single characters followed by ranges")
        void shouldParseMixedSingleCharsAndRanges() {
            assertCharSetContainsExactly("ae-f",
                CharRange.is('a'),
                CharRange.isIn('e', 'f')
            );
        }

        @Test
        @DisplayName("should parse a mix of ranges followed by single characters")
        void shouldParseMixedRangesAndSingleChars() {
            // This test confirms that the parsing order of different syntax types does not affect the final set.
            assertCharSetContainsExactly("e-fa",
                CharRange.is('a'),
                CharRange.isIn('e', 'f')
            );
        }

        @Test
        @DisplayName("should parse a complex string with multiple single characters and ranges")
        void shouldParseComplexMixOfCharsAndRanges() {
            assertCharSetContainsExactly("ae-fm-pz",
                CharRange.is('a'),
                CharRange.isIn('e', 'f'),
                CharRange.isIn('m', 'p'),
                CharRange.is('z')
            );
        }
    }
}