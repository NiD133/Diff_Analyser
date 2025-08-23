package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test case evaluates the {@link CharSequenceUtils#lastIndexOf(CharSequence, int, int)} method.
 */
public class CharSequenceUtils_ESTestTest5 {

    /**
     * Tests that lastIndexOf finds the correct character when the search
     * starts from an index that is after the character to be found.
     */
    @Test
    public void testLastIndexOfCharWithStartIndexFindsPreviousOccurrence() {
        // Arrange
        // The character 't' appears at indices 0, 3, and 8.
        CharSequence text = new StringBuilder("testing the test");
        char searchChar = 't';
        int startIndex = 10; // Start searching backwards from index 10 ('e').
        int expectedIndex = 8; // The method should find the 't' at index 8 and ignore the one at index 12.

        // Act
        int actualIndex = CharSequenceUtils.lastIndexOf(text, searchChar, startIndex);

        // Assert
        assertEquals(expectedIndex, actualIndex);
    }
}