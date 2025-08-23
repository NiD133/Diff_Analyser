package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CharRange}.
 *
 * Note: The original class name "CharRangeTestTest4" was renamed to "CharRangeTest"
 * to improve clarity and remove redundancy.
 */
public class CharRangeTest extends AbstractLangTest {

    @Test
    @DisplayName("CharRange.isIn() creates a normalized range when start and end characters are reversed")
    void isInShouldCreateNormalizedRangeForReversedArguments() {
        // Background: The documentation for CharRange.isIn(start, end) states that if the
        // arguments are in the wrong order, they are automatically reversed.
        // This test verifies that behavior.

        // Arrange: Define start and end characters in a reversed order.
        final char startChar = 'e';
        final char endChar = 'a';
        final String expectedToString = "a-e";

        // Act: Create a CharRange using the factory method under test.
        final CharRange range = CharRange.isIn(startChar, endChar);

        // Assert: The created range should be normalized (from 'a' to 'e') and not negated.
        assertAll("Properties of range from CharRange.isIn('e', 'a')",
            () -> assertEquals('a', range.getStart(), "Start should be the smaller character"),
            () -> assertEquals('e', range.getEnd(), "End should be the larger character"),
            () -> assertFalse(range.isNegated(), "The range should not be negated"),
            () -> assertEquals(expectedToString, range.toString(), "The string representation should be normalized")
        );
    }
}