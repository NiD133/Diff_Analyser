package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CharRange}.
 * Note: The original class name {@code CharRangeTestTest2} was preserved.
 * A more standard name would be {@code CharRangeTest}.
 */
public class CharRangeTestTest2 extends AbstractLangTest {

    @Test
    @DisplayName("CharRange.is(ch) should create a non-negated range for a single character")
    void is_createsSingleCharacterRange() {
        // Arrange
        final char testChar = 'a';

        // Act
        final CharRange range = CharRange.is(testChar);

        // Assert
        assertAll("A single-character range should have the correct properties",
            () -> assertEquals(testChar, range.getStart(), "The start character should be the input character."),
            () -> assertEquals(testChar, range.getEnd(), "The end character should be the input character."),
            () -> assertFalse(range.isNegated(), "The range should not be negated."),
            () -> assertEquals(String.valueOf(testChar), range.toString(), "The string representation should be the character itself.")
        );
    }
}