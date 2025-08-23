package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.nio.CharBuffer;

/**
 * This class contains tests for the {@link CharSequenceUtils} class.
 * This is a refactored version of a single, tool-generated test case.
 */
public class CharSequenceUtilsTest {

    @Test
    public void testLastIndexOf_searchForSelfWithStartIndexAtLength_returnsZero() {
        // Arrange
        // Use a CharBuffer to test with a non-String CharSequence implementation.
        // Using a readable string like "abc" makes the test's intent clearer than
        // the original's use of a buffer filled with null characters.
        final CharSequence sequence = CharBuffer.wrap("abc".toCharArray());
        final int startIndex = sequence.length(); // Start search from the end of the sequence

        // Act
        final int foundIndex = CharSequenceUtils.lastIndexOf(sequence, sequence, startIndex);

        // Assert
        // When searching for a sequence within itself, it should be found at the beginning (index 0).
        assertEquals(0, foundIndex);
    }
}