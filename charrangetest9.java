package org.apache.commons.lang3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link CharRange}.
 */
class CharRangeTest {

    @Test
    @DisplayName("isNotIn() with the same start and end character should create a negated single-character range")
    void isNotInWithSameCharacterShouldCreateNegatedSingleCharRange() {
        // Arrange
        final char character = 'a';
        final String expectedToString = "^a";

        // Act
        final CharRange range = CharRange.isNotIn(character, character);

        // Assert
        assertAll("Properties of a negated single-character range",
            () -> assertEquals(character, range.getStart(), "The start character should be 'a'"),
            () -> assertEquals(character, range.getEnd(), "The end character should be 'a'"),
            () -> assertTrue(range.isNegated(), "The range should be marked as negated"),
            () -> assertEquals(expectedToString, range.toString(), "The string representation should indicate a negated single character")
        );
    }
}