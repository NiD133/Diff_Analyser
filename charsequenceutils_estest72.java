package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link CharSequenceUtils}.
 */
public class CharSequenceUtilsRefactoredTest {

    @Test
    public void testLastIndexOfWithNonMatchingCharSequenceShouldReturnNotFound() {
        // Arrange
        String textToSearch = "A test string to search within.";

        // Create a search sequence that is almost identical to the textToSearch,
        // but with one character removed to ensure it does not match.
        StringBuilder nonMatchingSearchSequence = new StringBuilder(textToSearch);
        nonMatchingSearchSequence.deleteCharAt(10); // Deletes 'g' from "string"

        // The search will start from the end of the string.
        int startIndex = textToSearch.length();

        // Act
        // Attempt to find the last index of the modified sequence within the original, unmodified string.
        int actualIndex = CharSequenceUtils.lastIndexOf(textToSearch, nonMatchingSearchSequence, startIndex);

        // Assert
        // The non-matching sequence should not be found, so the result must be -1.
        assertEquals(-1, actualIndex);
    }
}