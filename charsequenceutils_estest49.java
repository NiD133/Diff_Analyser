package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link CharSequenceUtils}.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that lastIndexOf returns -1 when the search sequence does not match the source
     * within the specified search bounds.
     *
     * This test specifically covers a scenario where:
     * 1. The search sequence is very similar to the source sequence but has a character mismatch.
     * 2. The search is restricted by a 'startIndex' that is much smaller than the length of the
     *    search sequence, forcing the search to occur only at the very beginning of the source.
     */
    @Test
    public void lastIndexOfShouldReturnNotFoundForMismatchedSequenceWithRestrictiveStartIndex() {
        // Arrange
        final CharSequence source = "abc_def";
        // The search sequence is similar to the source but differs by one character ('X' instead of '_').
        final CharSequence search = "abcXdef";

        // A restrictive start index limits the search to the beginning of the source string.
        // Given the length of the search sequence (7), the only possible starting position for a
        // match within the search range [0, 2] is index 0.
        final int startIndex = 2;
        final int expectedIndex = -1;

        // Act
        // Search for "abcXdef" within "abc_def", starting the search from index 2 and going backward.
        final int actualIndex = CharSequenceUtils.lastIndexOf(source, search, startIndex);

        // Assert
        // The sequence is not found because the only possible match (starting at index 0) fails
        // due to a character mismatch at index 3 ('_' in source vs 'X' in search).
        assertEquals(expectedIndex, actualIndex);
    }
}