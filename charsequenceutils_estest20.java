package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for {@link CharSequenceUtils}.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that {@code lastIndexOf} finds the correct last occurrence of a sequence
     * when the text to search in is a repetition of that search sequence.
     *
     * <p>This test specifically covers the case where the starting index for the search
     * is greater than the length of the text. According to the method's contract,
     * this should cause the search to begin from the end of the text.</p>
     */
    @Test
    public void testLastIndexOfWithRepeatedSequenceAndStartIndexBeyondLength() {
        // Arrange
        // The sequence to search for.
        final String searchSequence = "', is neither of type Map.Entry nor an Array";
        final int searchSequenceLength = searchSequence.length(); // is 44

        // The text to search within, constructed by repeating the searchSequence twice.
        // The content becomes: searchSequence + searchSequence.
        // Occurrences of searchSequence are at index 0 and 44.
        final StringBuilder textToSearch = new StringBuilder(searchSequence);
        textToSearch.append(searchSequence);

        // A start index greater than the text length should be treated as starting from the end.
        final int startIndex = textToSearch.length() + 10; // 88 + 10 = 98

        // The expected index is the start of the *last* occurrence of the searchSequence.
        final int expectedIndex = searchSequenceLength; // Index 44

        // Act
        final int actualIndex = CharSequenceUtils.lastIndexOf(textToSearch, searchSequence, startIndex);

        // Assert
        assertEquals(expectedIndex, actualIndex);
    }
}