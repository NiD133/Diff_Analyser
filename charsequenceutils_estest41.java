package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests for {@link CharSequenceUtils}.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that {@link CharSequenceUtils#lastIndexOf(CharSequence, int, int)}
     * correctly finds a supplementary character (a character represented by a surrogate pair)
     * when it is at the beginning of the CharSequence and the search also starts there.
     */
    @Test
    public void testLastIndexOfWithSupplementaryCharacterFoundAtStart() {
        // Arrange
        // A supplementary character is a Unicode character outside the Basic Multilingual Plane (BMP),
        // with a code point > 0xFFFF. U+10000 is the first such character.
        // In Java, it's represented by a surrogate pair of two 'char's.
        final int supplementaryCodePoint = 0x10000; // 65536
        final CharSequence textWithSupplementaryChar = new StringBuilder().appendCodePoint(supplementaryCodePoint);

        final int startIndex = 0;
        final int expectedIndex = 0;

        // Act
        final int actualIndex = CharSequenceUtils.lastIndexOf(textWithSupplementaryChar, supplementaryCodePoint, startIndex);

        // Assert
        assertEquals("The supplementary character should be found at index 0.",
                expectedIndex, actualIndex);
    }
}