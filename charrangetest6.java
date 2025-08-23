package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link CharRange} class, focusing on its factory methods.
 */
class CharRangeTest {

    @Test
    @DisplayName("CharRange.isNot() should create a negated range for a single character")
    void isNot_createsNegatedSingleCharacterRange() {
        // Arrange
        final char testChar = 'a';
        final String expectedStringRepresentation = "^a";

        // Act
        final CharRange negatedRange = CharRange.isNot(testChar);

        // Assert
        assertAll("Properties of a negated single-character range",
            () -> assertEquals(testChar, negatedRange.getStart(), "The start character should match the input."),
            () -> assertEquals(testChar, negatedRange.getEnd(), "The end character should match the input."),
            () -> assertTrue(negatedRange.isNegated(), "The range should be marked as negated."),
            () -> assertEquals(expectedStringRepresentation, negatedRange.toString(), "The string representation should indicate a negated range.")
        );
    }
}