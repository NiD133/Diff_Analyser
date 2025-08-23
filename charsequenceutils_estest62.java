package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link CharSequenceUtils}.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that {@link CharSequenceUtils#indexOf(CharSequence, int, int)}
     * correctly finds a character located at the beginning of the sequence
     * when the search starts at index 0.
     */
    @Test
    public void indexOf_shouldFindCharacterAtBeginningOfCharSequence() {
        // Arrange
        // A String containing the null character is a simple and clear CharSequence.
        final CharSequence text = "\0";
        final int charToFind = '\0';
        final int startIndex = 0;

        // Act
        final int foundIndex = CharSequenceUtils.indexOf(text, charToFind, startIndex);

        // Assert
        final int expectedIndex = 0;
        assertEquals(expectedIndex, foundIndex);
    }
}