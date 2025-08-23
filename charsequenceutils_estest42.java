package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test class contains an improved version of a test case for CharSequenceUtils.
 * The original test was auto-generated and lacked clarity.
 */
public class CharSequenceUtils_ESTestTest42 extends CharSequenceUtils_ESTest_scaffolding {

    /**
     * Tests that {@code CharSequenceUtils.lastIndexOf} returns -1 when searching for a code point
     * that is not present in the CharSequence, starting from a given index.
     */
    @Test
    public void lastIndexOf_whenCodePointIsNotFound_shouldReturnMinusOne() {
        // Arrange
        final CharSequence text = new StringBuilder("', ss }either of typp Map.Entry nor an Array");
        // The search character is a supplementary code point (U+10FFFE) that does not exist in the text.
        final int nonExistentCodePoint = 1114110;
        final int searchStartIndex = 25;

        // Act
        final int actualIndex = CharSequenceUtils.lastIndexOf(text, nonExistentCodePoint, searchStartIndex);

        // Assert
        final int expectedIndex = -1; // The method should return -1 to indicate "not found".
        assertEquals("Expected -1 because the code point does not exist in the CharSequence.", expectedIndex, actualIndex);
    }
}