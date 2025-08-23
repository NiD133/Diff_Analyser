package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link org.apache.commons.lang3.CharSequenceUtils}.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that CharSequenceUtils.indexOf() returns -1 when the starting index
     * is greater than the length of the CharSequence. This specific case uses an
     * empty sequence, where any positive start index is out of bounds.
     */
    @Test
    public void indexOf_withStartIndexGreaterThanLength_shouldReturnNotFound() {
        // Arrange
        final CharSequence emptySequence = new StringBuilder();
        final int searchChar = 'a'; // The character being searched for is irrelevant in this case.
        final int startIndex = 1;   // Any index > 0 is out of bounds for an empty sequence.

        // Act
        final int actualIndex = CharSequenceUtils.indexOf(emptySequence, searchChar, startIndex);

        // Assert
        final int expectedIndex = -1; // -1 indicates "not found".
        assertEquals("indexOf should return -1 when the start index is out of bounds.",
                expectedIndex, actualIndex);
    }
}