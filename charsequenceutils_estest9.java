package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This class contains test cases for the {@link CharSequenceUtils} class,
 * focusing on the lastIndexOf method.
 */
public class CharSequenceUtilsRefactoredTest {

    /**
     * Tests that lastIndexOf returns -1 when the search sequence does not exist
     * in the source CharSequence.
     */
    @Test
    public void lastIndexOf_whenSearchSequenceIsNotFound_shouldReturnNegativeOne() {
        // Arrange: Define the source text, the sequence to search for, and the expected result.
        final CharSequence sourceText = "0.0";
        final CharSequence searchSequence = "@3AXK#eny?[;j'";
        final int startIndex = 0;
        final int expectedResult = -1; // -1 indicates that the sequence was not found.

        // Act: Call the lastIndexOf method with the prepared data.
        final int actualResult = CharSequenceUtils.lastIndexOf(sourceText, searchSequence, startIndex);

        // Assert: Verify that the method returned the expected result.
        assertEquals(expectedResult, actualResult);
    }
}