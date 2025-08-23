package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link CharSequenceUtils}.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that indexOf returns -1 when searching for a supplementary character
     * that is not present in the CharSequence, especially when the search
     * starts from a negative index. According to the Javadoc, a negative start
     * index should be treated as 0.
     */
    @Test
    public void indexOfShouldReturnNotFoundForMissingSupplementaryCharWithNegativeStartIndex() {
        // Arrange
        // A CharSequence containing a supplementary character (the max code point).
        final StringBuilder text = new StringBuilder("somePrefix");
        text.appendCodePoint(Character.MAX_CODE_POINT);

        // The supplementary character to search for (U+10000), which is not in the text.
        final int searchChar = 0x10000; // 65536 is the first supplementary code point.

        // A negative start index, which should be treated as 0.
        final int startIndex = -1;
        final int expectedIndex = -1; // The "not found" result.

        // Act
        final int actualIndex = CharSequenceUtils.indexOf(text, searchChar, startIndex);

        // Assert
        assertEquals("indexOf should return -1 for a character not found.", expectedIndex, actualIndex);
    }
}