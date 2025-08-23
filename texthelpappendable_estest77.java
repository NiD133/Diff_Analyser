package org.apache.commons.cli.help;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the static utility method {@link TextHelpAppendable#indexOfWrap(CharSequence, int, int)}.
 */
public class TextHelpAppendableIndexOfWrapTest {

    /**
     * Tests that indexOfWrap correctly identifies the last whitespace character
     * that falls within the search window defined by [startPos, startPos + width).
     */
    @Test
    public void indexOfWrap_shouldReturnIndexOfLastSpace_whenSpaceExistsInSearchWindow() {
        // Arrange
        // The method searches for a wrap point in the substring starting at index 3
        // with a maximum length of 3 characters (i.e., from index 3 to 6).
        //
        // Text:  W i d t h   m u s t ...
        // Index: 0 1 2 3 4 5 6 7 8 9
        // Window:      [t h  ]
        //
        // The last space character within this window is at index 5.
        final String text = "Width must be greater than 0";
        final int searchWidth = 3;
        final int startPosition = 3;
        final int expectedWrapIndex = 5;

        // Act
        final int actualWrapIndex = TextHelpAppendable.indexOfWrap(text, searchWidth, startPosition);

        // Assert
        assertEquals("The wrap index should be the position of the last space within the search window.",
                expectedWrapIndex, actualWrapIndex);
    }
}