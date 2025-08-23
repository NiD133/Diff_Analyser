package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link CharSequenceUtils} class.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that CharSequenceUtils.indexOf behaves like String.indexOf when searching
     * for an empty string with a start index greater than the source sequence's length.
     * In this specific edge case, the method should return the length of the source sequence.
     */
    @Test(timeout = 4000)
    public void indexOf_emptySearchSequence_withStartIndexGreaterThanLength_shouldReturnSourceLength() {
        // Arrange
        final CharSequence sourceSequence = new StringBuilder("A test string");
        final CharSequence emptySearchSequence = new StringBuffer(); // An empty sequence to search for
        final int startIndex = Integer.MAX_VALUE; // A start index guaranteed to be out of bounds

        // Act
        final int actualIndex = CharSequenceUtils.indexOf(sourceSequence, emptySearchSequence, startIndex);

        // Assert
        // The expected index is the length of the source sequence, not -1.
        assertEquals(sourceSequence.length(), actualIndex);
    }
}