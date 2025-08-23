package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link CharSequenceUtils}.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that {@code lastIndexOf} correctly finds a match at the beginning of a CharSequence
     * when the starting search index is greater than the sequence's length.
     * The behavior should be consistent with {@link String#lastIndexOf(String, int)},
     * which treats an out-of-bounds starting index as the maximum possible index.
     */
    @Test
    public void lastIndexOfShouldFindMatchWhenStartIndexIsGreaterThanLength() {
        // Arrange
        final CharSequence text = new StringBuilder("find me");
        final CharSequence searchSequence = "find me";
        
        // Use a starting index that is intentionally larger than the text's length.
        final int startIndex = text.length() + 50;
        final int expectedIndex = 0;

        // Act
        final int actualIndex = CharSequenceUtils.lastIndexOf(text, searchSequence, startIndex);

        // Assert
        assertEquals("Should find the sequence at the beginning", expectedIndex, actualIndex);
    }
}