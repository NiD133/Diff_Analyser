package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains test cases for the {@link CharSequenceUtils} class.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that indexOf returns the source's length (0) when searching for an
     * empty CharSequence in an empty source with a start index greater than the source length.
     * This behavior is consistent with {@link String#indexOf(String, int)}.
     */
    @Test
    public void indexOf_emptySearchInEmptySourceWithStartIndexOutOfBounds_shouldReturnZero() {
        // Arrange
        final CharSequence sourceText = "";
        final CharSequence textToFind = new StringBuilder("");
        final int startIndex = 8; // An index well beyond the source length
        final int expectedIndex = 0; // Expect the length of the source CharSequence

        // Act
        final int actualIndex = CharSequenceUtils.indexOf(sourceText, textToFind, startIndex);

        // Assert
        assertEquals(
            "Searching for an empty sequence with an out-of-bounds start index should return the source's length.",
            expectedIndex,
            actualIndex
        );
    }
}