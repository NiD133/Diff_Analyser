package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.nio.CharBuffer;

/**
 * This class contains improved tests for the {@link CharSequenceUtils} class.
 */
public class CharSequenceUtilsImprovedTest {

    /**
     * Tests that CharSequenceUtils.indexOf returns -1 when the search sequence
     * is longer than the source sequence, even with a negative start index.
     * A negative start index is treated as 0 by the method.
     */
    @Test
    public void indexOfShouldReturnNotFoundWhenSearchSequenceIsLongerThanSource() {
        // Arrange
        CharSequence sourceText = CharBuffer.wrap(new char[]{'a', 'b', 'c'});
        CharSequence searchText = "abcde"; // This is longer than sourceText
        int invalidStartIndex = -1;

        // Act
        int result = CharSequenceUtils.indexOf(sourceText, searchText, invalidStartIndex);

        // Assert
        final int NOT_FOUND = -1;
        assertEquals("Expected -1 (not found) because the search text is longer than the source text.", NOT_FOUND, result);
    }
}