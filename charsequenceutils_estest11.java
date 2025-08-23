package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.nio.CharBuffer;

/**
 * Tests for {@link CharSequenceUtils}.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that lastIndexOf returns the length of the CharSequence when
     * searching for an empty string with a starting index greater than the sequence's length.
     *
     * <p>This behavior is consistent with {@link String#lastIndexOf(String, int)}, which
     * returns the start index for an empty search string, but our utility method
     * clamps an out-of-bounds start index to the sequence's length.</p>
     */
    @Test
    public void lastIndexOf_withEmptySearchStringAndStartIndexOutOfBounds_shouldReturnSequenceLength() {
        // Arrange
        final CharSequence textToSearch = CharBuffer.wrap("ab"); // A sequence of length 2
        final CharSequence emptySearchString = "";
        final int startIndex = 4; // An index greater than the length of textToSearch
        
        final int expectedIndex = textToSearch.length(); // Expect the result to be clamped to the length (2)

        // Act
        final int actualIndex = CharSequenceUtils.lastIndexOf(textToSearch, emptySearchString, startIndex);

        // Assert
        assertEquals("Searching for an empty string with a start index > length should return the length",
                expectedIndex, actualIndex);
    }
}