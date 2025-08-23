package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test suite contains tests for the {@link CharSequenceUtils} class.
 * This specific test case focuses on the behavior of the lastIndexOf method.
 */
public class CharSequenceUtils_ESTestTest8 extends CharSequenceUtils_ESTest_scaffolding {

    /**
     * Tests that {@code CharSequenceUtils.lastIndexOf} returns -1 when the search
     * sequence is not found within the text, particularly when the provided
     * starting index is greater than the length of the text being searched.
     */
    @Test
    public void lastIndexOf_shouldReturnNotFound_whenSearchStringIsAbsentAndStartIndexIsOutOfBounds() {
        // Arrange
        final CharSequence textToSearch = "A simple string to search within.";
        final CharSequence searchSequence = "nonexistent";

        // Use a starting index that is intentionally out of the text's bounds.
        final int startIndex = textToSearch.length() + 10;
        final int expectedIndex = -1; // The value indicating "not found".

        // Act
        final int actualIndex = CharSequenceUtils.lastIndexOf(textToSearch, searchSequence, startIndex);

        // Assert
        assertEquals("Expected -1 when the search sequence is not found.", expectedIndex, actualIndex);
    }
}