package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test case has been improved for better understandability.
 */
public class CharSequenceUtilsImprovedTest {

    /**
     * Tests that lastIndexOf returns -1 when the starting search index is greater than the length of the CharSequence.
     *
     * According to the Javadoc, a starting index beyond the length of the string should be handled
     * by starting the search from the end of the string. In this case, since the character
     * is not found in the string, the expected result is -1.
     */
    @Test
    public void lastIndexOfWithStartIndexGreaterThanLengthShouldReturnNotFound() {
        // Arrange
        final CharSequence text = "6KY>-,V";
        final int searchCharNotInText = 1403; // A character not present in the text.
        final int startIndexBeyondLength = text.length() + 100; // An index clearly out of bounds.

        // Act
        final int actualIndex = CharSequenceUtils.lastIndexOf(text, searchCharNotInText, startIndexBeyondLength);

        // Assert
        final int expectedIndex = -1;
        assertEquals(expectedIndex, actualIndex);
    }
}