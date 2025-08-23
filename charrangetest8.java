package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CharRange}.
 */
// The original class name 'CharRangeTestTest8' was redundant and non-descriptive.
// Renaming it to 'CharRangeTest' is standard practice.
public class CharRangeTest extends AbstractLangTest {

    @Test
    @DisplayName("CharRange.isNotIn should create a correct negated range when start and end characters are reversed")
    void isNotInFactoryShouldCorrectlyHandleReversedArguments() {
        // --- Arrange ---
        // The Javadoc for isNotIn() states that if start > end, the arguments are swapped.
        // We test this behavior by providing 'e' as the start and 'a' as the end.
        final char largerChar = 'e';
        final char smallerChar = 'a';
        final String expectedToString = "^a-e";

        // --- Act ---
        // Create a negated range with reversed start and end characters.
        final CharRange range = CharRange.isNotIn(largerChar, smallerChar);

        // --- Assert ---
        // Verify that the factory method correctly created a negated range.
        assertTrue(range.isNegated(), "The range created by isNotIn() should be negated.");

        // Verify that the start and end characters were automatically swapped to the correct order.
        assertEquals(smallerChar, range.getStart(), "Start character should be the smaller of the two inputs.");
        assertEquals(largerChar, range.getEnd(), "End character should be the larger of the two inputs.");

        // Verify the string representation matches the expected format for a negated, ordered range.
        assertEquals(expectedToString, range.toString(), "String representation should reflect the ordered range.");
    }
}