package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link CharSequenceUtils}.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that {@code indexOf} correctly finds a supplementary Unicode character
     * when the search starts from a negative index.
     *
     * <p>According to the Javadoc, a negative start index is treated as 0,
     * meaning the entire CharSequence should be searched from the beginning.</p>
     *
     * <p>A supplementary character is a Unicode character that is represented by
     * two {@code char} values (a surrogate pair) in Java.</p>
     */
    @Test
    public void testIndexOfFindsSupplementaryCharWithNegativeStartIndex() {
        // Arrange
        // U+10FFFF is the maximum valid Unicode code point and serves as a supplementary character.
        final int supplementaryCodePoint = 0x10FFFF;
        final String prefix = "search-prefix";
        
        final StringBuilder text = new StringBuilder(prefix);
        text.appendCodePoint(supplementaryCodePoint);

        final int expectedIndex = prefix.length(); // The character is appended after the prefix.
        final int negativeStartIndex = -1;

        // Act
        final int actualIndex = CharSequenceUtils.indexOf(text, supplementaryCodePoint, negativeStartIndex);

        // Assert
        assertEquals(expectedIndex, actualIndex);
    }
}