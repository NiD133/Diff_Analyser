package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the factory method {@link CharRange#isNotIn(char, char)}.
 */
public class CharRangeTestTest7 extends AbstractLangTest {

    @Test
    @DisplayName("CharRange.isNotIn() should create a negated range with correct properties")
    void isNotInShouldCreateCorrectNegatedRange() {
        // Arrange
        final char startChar = 'a';
        final char endChar = 'e';
        final String expectedToString = "^a-e";

        // Act
        final CharRange range = CharRange.isNotIn(startChar, endChar);

        // Assert
        assertTrue(range.isNegated(), "The range should be marked as negated.");
        assertEquals(startChar, range.getStart(), "The start character should match the input.");
        assertEquals(endChar, range.getEnd(), "The end character should match the input.");
        assertEquals(expectedToString, range.toString(), "The string representation should indicate a negated range.");
    }
}