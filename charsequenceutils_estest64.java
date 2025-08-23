package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for {@link CharSequenceUtils}.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that CharSequenceUtils.indexOf returns -1 when the starting index
     * is greater than the length of the CharSequence.
     */
    @Test
    public void indexOfShouldReturnNotFoundForStartIndexGreaterThanLength() {
        // Arrange
        final CharSequence sequence = new StringBuilder(); // An empty sequence has length 0
        final int searchChar = 'a';
        final int startIndex = 1; // An index greater than the sequence's length

        // Act
        final int result = CharSequenceUtils.indexOf(sequence, searchChar, startIndex);

        // Assert
        assertEquals("indexOf should return -1 for a start index beyond the sequence length", -1, result);
    }
}