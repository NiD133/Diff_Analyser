package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests for {@link CharSequenceUtils}.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that {@code CharSequenceUtils.indexOf} returns -1 when searching for a
     * supplementary character that is not present in the CharSequence, especially
     * when the search starts from a negative index.
     *
     * According to the Javadoc, a negative start index is treated as 0, meaning
     * the entire sequence is searched. This test verifies that behavior.
     */
    @Test
    public void testIndexOfForMissingSupplementaryCharWithNegativeStartIndex() {
        // Arrange
        // A CharSequence containing a supplementary character (U+1001D).
        // A supplementary character is one outside the Basic Multilingual Plane.
        final CharSequence textWithSupplementaryChar = new StringBuilder().appendCodePoint(0x1001D);

        // The character to search for is a different supplementary character (U+10000).
        final int charToFind = 0x10000;

        // A negative start index should cause the search to start from the beginning.
        final int negativeStartIndex = -1;
        final int expectedResult = -1; // Expected result for a failed search.

        // Act
        final int actualResult = CharSequenceUtils.indexOf(textWithSupplementaryChar, charToFind, negativeStartIndex);

        // Assert
        assertEquals(expectedResult, actualResult);
    }
}