package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CharSet} focusing on the Javadoc examples for negation and literal carets.
 */
public class CharSetTest extends AbstractLangTest {

    @Nested
    @DisplayName("Tests for negation and literal caret syntax from Javadoc examples")
    class JavadocExamplesTests {

        @Test
        @DisplayName("`^a-c` (negated range) excludes chars in range, includes chars outside")
        void testNegatedRange() {
            // Verifies Javadoc examples:
            // CharSet.getInstance("^a-c").contains('a') ==> false
            // CharSet.getInstance("^a-c").contains('d') ==> true
            final CharSet negatedSet = CharSet.getInstance("^a-c");

            assertFalse(negatedSet.contains('a'),
                "The negated set '^a-c' should NOT contain 'a'.");
            assertTrue(negatedSet.contains('d'),
                "The negated set '^a-c' SHOULD contain 'd'.");
        }

        @Test
        @DisplayName("`^^a-c` (double negation) includes 'a', but excludes '^'")
        void testDoubleNegation() {
            // Verifies Javadoc examples:
            // CharSet.getInstance("^^a-c").contains('a') ==> true
            // CharSet.getInstance("^^a-c").contains('^') ==> false
            final CharSet doubleNegatedSet = CharSet.getInstance("^^a-c");

            assertTrue(doubleNegatedSet.contains('a'),
                "The double-negated set '^^a-c' SHOULD contain 'a'.");
            assertFalse(doubleNegatedSet.contains('^'),
                "The double-negated set '^^a-c' should NOT contain the negation character '^'.");
        }

        @Test
        @DisplayName("`^a-cd-f` (combined ranges) includes chars from the positive range")
        void testCombinedNegatedAndPositiveRanges() {
            // Verifies Javadoc example:
            // CharSet.getInstance("^a-cd-f").contains('d') ==> true
            final CharSet combinedSet = CharSet.getInstance("^a-cd-f");

            assertTrue(combinedSet.contains('d'),
                "The combined set '^a-cd-f' SHOULD contain 'd' from the 'd-f' range.");
        }

        @Test
        @DisplayName("`a-c^` (literal caret at the end) includes '^'")
        void testLiteralCaretAtEnd() {
            // Verifies Javadoc example:
            // CharSet.getInstance("a-c^").contains('^') ==> true
            final CharSet setWithLiteralCaret = CharSet.getInstance("a-c^");

            assertTrue(setWithLiteralCaret.contains('^'),
                "A caret at the end of the string, as in 'a-c^', should be treated as a literal character.");
        }

        @Test
        @DisplayName("`^`, `a-c` (literal caret as separate argument) includes '^'")
        void testLiteralCaretAsSeparateArgument() {
            // Verifies Javadoc example:
            // CharSet.getInstance("^", "a-c").contains('^') ==> true
            final CharSet setWithLiteralCaret = CharSet.getInstance("^", "a-c");

            assertTrue(setWithLiteralCaret.contains('^'),
                "A caret provided as a separate string argument should be treated as a literal character.");
        }
    }
}