package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for {@link CharSequenceUtils}.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that CharSequenceUtils.indexOf returns -1 when searching for a supplementary
     * character in an empty CharSequence, even with a negative start index.
     *
     * According to the method's documentation, a negative start index is treated as 0,
     * and searching any character in an empty sequence should result in "not found" (-1).
     */
    @Test
    public void indexOf_withEmptySequenceAndNegativeStart_shouldReturnNotFound() {
        // Arrange
        final CharSequence emptySequence = new StringBuilder();
        // A supplementary character (U+10023) to test code point handling.
        final int supplementaryCharToFind = 65571;
        // Per Javadoc, any negative start index is treated as 0.
        final int negativeStartIndex = -2194;
        final int expectedNotFoundIndex = -1;

        // Act
        final int actualIndex = CharSequenceUtils.indexOf(emptySequence, supplementaryCharToFind, negativeStartIndex);

        // Assert
        assertEquals(expectedNotFoundIndex, actualIndex);
    }
}