package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link CharSequenceUtils}.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that lastIndexOf correctly finds a prefix at the beginning of a CharSequence
     * even when the search starts from the end of the sequence.
     */
    @Test
    public void testLastIndexOfFindsPrefixWhenStartingSearchFromEnd() {
        // Arrange
        final CharSequence sourceText = "prefix_and_suffix";
        final CharSequence prefixToSearch = "prefix_";
        final int startIndex = sourceText.length() - 1; // Start search from the end of the source text
        final int expectedIndex = 0;

        // Act
        // Search backwards for the prefix within the source text.
        final int actualIndex = CharSequenceUtils.lastIndexOf(sourceText, prefixToSearch, startIndex);

        // Assert
        // The method should find the prefix at the beginning of the source text.
        assertEquals(expectedIndex, actualIndex);
    }
}