package org.apache.commons.cli.help;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the static utility methods of {@link TextHelpAppendable}.
 */
public class TextHelpAppendableTest {

    /**
     * Tests that indexOfWrap returns the starting position when the search width is 1
     * and no whitespace is found within that single-character range.
     */
    @Test
    public void indexOfWrapWithWidthOfOneShouldReturnStartPosition() {
        // Arrange
        // The text has a space at index 27, just outside the search range.
        final String text = "Width must be greater than 0";
        final int startPos = 26;
        final int searchWidth = 1;
        
        // The search for a wrap point starts at index 26. With a width of 1,
        // the method only considers the character at index 26 ('n').
        // The expected result is the starting position itself, as no wrap
        // point is found within this narrow window.
        final int expectedWrapIndex = 26;

        // Act
        final int actualWrapIndex = TextHelpAppendable.indexOfWrap(text, searchWidth, startPos);

        // Assert
        assertEquals(expectedWrapIndex, actualWrapIndex);
    }
}