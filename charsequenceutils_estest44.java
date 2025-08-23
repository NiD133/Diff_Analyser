package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link CharSequenceUtils}.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that lastIndexOf returns -1 when searching within an empty CharSequence.
     * The specific character being searched for is irrelevant in this case.
     */
    @Test
    public void lastIndexOf_withEmptyCharSequence_shouldReturnNotFound() {
        // Arrange
        final CharSequence emptyCharSequence = new StringBuilder();
        // This value is an invalid Unicode code point, but any character would yield the same result.
        final int searchChar = 1_114_122; 
        final int startIndex = 0;
        final int expected = -1;

        // Act
        final int actual = CharSequenceUtils.lastIndexOf(emptyCharSequence, searchChar, startIndex);

        // Assert
        assertEquals("Searching in an empty CharSequence should always return -1.", expected, actual);
    }
}