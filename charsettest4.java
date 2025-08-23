package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Contains tests for {@link CharSet} focusing on complex or unusual constructor syntax.
 *
 * Note: The original class name `CharSetTestTest4` is kept, but a more descriptive
 * name like `CharSetComplexConstructorTest` would be preferable.
 */
public class CharSetTestTest4 extends AbstractLangTest {

    @Nested
    @DisplayName("Tests for CharSet constructor with ranges involving the caret character '^' as a literal")
    class CaretAsLiteralTests {

        @Test
        @DisplayName("Input 'a-^c' should create a set with range 'a' to '^' and character 'c'")
        void testConstructor_withRangeIncludingCaretAndSingleChar() {
            // The string "a-^c" is parsed as two definitions:
            // 1. "a-^": A range from 'a' to '^'. The parser normalizes this to the range from '^' to 'a'.
            // 2. "c": The single character 'c'.
            final CharSet charSet = CharSet.getInstance("a-^c");

            // Verify characters within the defined set
            assertTrue(charSet.contains('^'), "Range 'a-^' should contain its boundary '^'");
            assertTrue(charSet.contains('_'), "Range 'a-^' should contain characters between its boundaries");
            assertTrue(charSet.contains('a'), "Range 'a-^' should contain its boundary 'a'");
            assertTrue(charSet.contains('c'), "The set should contain the single character 'c'");

            // Verify a character outside the set
            assertFalse(charSet.contains('b'), "'b' is not in the range '^'-'a' nor is it 'c'");
        }

        @Test
        @DisplayName("Input '^-b' should create a set with range '^' to 'b'")
        void testConstructor_withRangeStartingWithCaret() {
            // The string "^-b" is parsed as a single range from '^' to 'b'.
            // The initial caret is not a negation operator because it is followed by a hyphen.
            final CharSet charSet = CharSet.getInstance("^-b");

            // Verify characters within the defined set
            assertTrue(charSet.contains('^'), "Range '^-b' should contain its boundary '^'");
            assertTrue(charSet.contains('_'), "Range '^-b' should contain characters between its boundaries");
            assertTrue(charSet.contains('b'), "Range '^-b' should contain its boundary 'b'");

            // Verify a character outside the set
            assertFalse(charSet.contains('A'), "'A' is outside the range '^-b'");
        }

        @Test
        @DisplayName("Input 'b-^' should create a set with range 'b' to '^'")
        void testConstructor_withRangeEndingWithCaret() {
            // The string "b-^" is parsed as a single range from 'b' to '^'.
            // The parser normalizes this to the range from '^' to 'b'.
            final CharSet charSet = CharSet.getInstance("b-^");

            // Verify characters within the defined set
            assertTrue(charSet.contains('b'), "Range 'b-^' should contain its boundary 'b'");
            assertTrue(charSet.contains('^'), "Range 'b-^' should contain its boundary '^'");
            assertTrue(charSet.contains('a'), "Range 'b-^' should contain characters between its boundaries");

            // Verify a character outside the set
            assertFalse(charSet.contains('c'), "'c' is outside the range 'b-^'");
        }
    }

    @Nested
    @DisplayName("Tests for CharSet constructor with negated ranges and complex combinations")
    class NegatedAndComplexRangeTests {

        @Test
        @DisplayName("Input '^a-^c' should create a set with a negated range and a single character")
        void testConstructor_withNegatedRangeAndSingleChar() {
            // The string "^a-^c" is parsed as two definitions:
            // 1. "^a-^": A negated range from 'a' to '^'. This includes all characters EXCEPT those from '^' to 'a'.
            // 2. "c": The single character 'c'.
            // The final set is the union of these two definitions.
            final CharSet charSet = CharSet.getInstance("^a-^c");

            // Verify characters included by the union
            assertTrue(charSet.contains('b'), "'b' is outside the negated range '^'-'a', so it should be in the set");
            assertTrue(charSet.contains('c'), "'c' is explicitly included in the set");

            // Verify characters excluded by the negated range
            assertFalse(charSet.contains('^'), "'^' is a boundary of the negated range, so it should be excluded");
            assertFalse(charSet.contains('_'), "'_' is inside the negated range, so it should be excluded");
        }

        @Test
        @DisplayName("Input 'a- ^-- ' should create a set containing almost all characters")
        void testConstructor_withOverlappingPositiveAndNegatedRanges() {
            // The string "a- ^-- " is parsed as a union of two definitions:
            // 1. "a- ": A range from 'a' to ' ' (space), normalized to ' ' through 'a'.
            // 2. "^-- ": A negated range from '-' to ' ' (space), normalized to NOT (' ' through '-').
            final CharSet charSet = CharSet.getInstance("a- ^-- ");

            // The union of (' ' to 'a') and (NOT (' ' to '-')) covers a very large set of characters.
            // We test a few sample characters that should be present due to one of the definitions.
            assertTrue(charSet.contains('#'), "'#' is included by the negated range part");
            assertTrue(charSet.contains('^'), "'^' is included by the positive range part");
            assertTrue(charSet.contains('a'), "'a' is included by the positive range part");
            assertTrue(charSet.contains('*'), "'*' is included by the negated range part");
            assertTrue(charSet.contains('A'), "'A' is included by the positive range part");
        }
    }
}