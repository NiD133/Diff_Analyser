package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link CharSequenceUtils} class.
 * Note: The original test class name "CharSequenceUtils_ESTestTest17" was preserved,
 * but in a real-world scenario, it should be simplified to "CharSequenceUtilsTest".
 */
public class CharSequenceUtils_ESTestTest17 {

    /**
     * Tests that indexOf returns -1 when searching an empty CharSequence.
     *
     * This test verifies that even with edge-case inputs like a negative search
     * character and a negative start index, the method correctly returns -1 (not found)
     * for an empty sequence. According to the method's contract, a negative start
     * index is treated as 0.
     */
    @Test
    public void indexOf_onEmptySequenceWithNegativeStartIndex_shouldReturnNotFound() {
        // Arrange
        final CharSequence emptySequence = new StringBuilder();
        final int negativeSearchChar = -1; // An arbitrary negative (invalid) character code.
        final int negativeStartIndex = -1; // A negative start index should be treated as 0.
        final int expectedIndex = -1;      // The constant for "not found".

        // Act
        final int actualIndex = CharSequenceUtils.indexOf(emptySequence, negativeSearchChar, negativeStartIndex);

        // Assert
        assertEquals("indexOf on an empty sequence should always return -1", expectedIndex, actualIndex);
    }
}