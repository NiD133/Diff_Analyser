package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test suite contains improved, human-readable tests for {@link CharSequenceUtils}.
 * The original test was auto-generated and has been refactored for clarity and maintainability.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that lastIndexOf returns the start index when the search sequence is empty.
     * This behavior is consistent with {@link String#lastIndexOf(String, int)}.
     */
    @Test
    public void lastIndexOf_withEmptySearchSequence_shouldReturnStartIndex() {
        // Arrange
        final CharSequence textToSearch = "a\u0000b"; // A non-empty sequence containing the original character
        final CharSequence emptySearchSequence = "";
        final int startIndex = 0;
        final int expectedIndex = 0;

        // Act
        final int actualIndex = CharSequenceUtils.lastIndexOf(textToSearch, emptySearchSequence, startIndex);

        // Assert
        assertEquals("Searching for an empty string should return the start index.", expectedIndex, actualIndex);
    }
}