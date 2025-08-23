package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CharRange} focusing on the creation of a single-character range.
 */
// The original class name "CharRangeTestTest5" is unconventional,
// likely a result of automated test generation. A more standard name
// would be "CharRangeTest".
public class CharRangeTestTest5 extends AbstractLangTest {

    @Test
    @DisplayName("CharRange.isIn() with identical start and end characters should create a valid single-character range")
    void isInWithSameStartAndEndShouldCreateSingleCharacterRange() {
        // Arrange
        final char testChar = 'a';

        // Act
        final CharRange range = CharRange.isIn(testChar, testChar);

        // Assert
        // Verify the properties of the single-character range
        assertEquals(testChar, range.getStart(), "The start character should match the input.");
        assertEquals(testChar, range.getEnd(), "The end character should match the input.");
        assertFalse(range.isNegated(), "A range created with isIn() should not be negated.");
        assertEquals(String.valueOf(testChar), range.toString(), "The string representation should be the character itself.");
    }
}