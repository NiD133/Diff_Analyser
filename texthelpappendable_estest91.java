package org.apache.commons.cli.help;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TextHelpAppendable_ESTestTest91 extends TextHelpAppendable_ESTest_scaffolding {

    /**
     * Tests that indexOfWrap returns the starting position immediately if the character
     * at that position is a line-breaking character (like a newline).
     */
    @Test
    public void indexOfWrapShouldReturnStartIndexWhenCharacterIsBreakChar() {
        // Arrange
        // The text contains a newline character '\n' at index 1.
        // According to the TextHelpAppendable implementation, '\n' is a break character.
        final CharSequence textWithBreakChar = "a\nbc";
        final int searchWidth = 10; // A width larger than the string to not affect the outcome.
        final int startPosition = 1;

        // Act
        final int wrapIndex = TextHelpAppendable.indexOfWrap(textWithBreakChar, searchWidth, startPosition);

        // Assert
        // The method should find the break character at the starting position and return that index.
        assertEquals("The wrap index should be the start position itself", startPosition, wrapIndex);
    }
}