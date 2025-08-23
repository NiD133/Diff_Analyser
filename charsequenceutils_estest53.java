package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for {@link CharSequenceUtils}.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that lastIndexOf returns -1 when the starting search index is negative.
     * This behavior is consistent with the contract of similar methods in the Java standard library.
     */
    @Test
    public void lastIndexOfWithNegativeStartIndexShouldReturnNotFound() {
        // Arrange
        final CharSequence text = new StringBuffer("abc");
        final CharSequence searchString = new StringBuffer("a");
        final int negativeStartIndex = -459;
        final int expectedIndex = -1; // The expected result for a "not found" scenario.

        // Act
        final int actualIndex = CharSequenceUtils.lastIndexOf(text, searchString, negativeStartIndex);

        // Assert
        assertEquals("A negative start index should always result in -1.", expectedIndex, actualIndex);
    }
}