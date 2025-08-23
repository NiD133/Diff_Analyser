package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link CharSequenceUtils} class.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that lastIndexOf returns -1 when the CharSequence to be searched is null.
     * This confirms the null-safe behavior of the method.
     */
    @Test
    public void lastIndexOf_withNullCharSequence_shouldReturnNegativeOne() {
        // Arrange
        final CharSequence searchSequence = "any sequence";
        final int startPosition = 0; // The start position is irrelevant when the input is null.
        final int expectedIndex = -1;

        // Act
        final int actualIndex = CharSequenceUtils.lastIndexOf(null, searchSequence, startPosition);

        // Assert
        assertEquals("The method should return -1 for a null CharSequence.", expectedIndex, actualIndex);
    }
}