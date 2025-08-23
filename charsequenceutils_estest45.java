package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.lang3.CharSequenceUtils}.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that lastIndexOf returns -1 when the starting search index is negative.
     *
     * <p>According to the Javadoc for {@link CharSequenceUtils#lastIndexOf(CharSequence, int, int)},
     * a negative start index should immediately result in -1, regardless of the CharSequence
     * or the character being searched for.</p>
     */
    @Test
    public void testLastIndexOfWithNegativeStartIndexReturnsNegativeOne() {
        // Arrange
        final CharSequence emptySequence = new StringBuilder();
        // An arbitrary invalid character code point is used to show it doesn't affect the outcome.
        final int anySearchChar = -76;
        final int negativeStartIndex = -76;
        final int expectedIndex = -1;

        // Act
        final int actualIndex = CharSequenceUtils.lastIndexOf(emptySequence, anySearchChar, negativeStartIndex);

        // Assert
        assertEquals("Expected -1 because the start index is negative.", expectedIndex, actualIndex);
    }
}