package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * This class contains tests for the CharSequenceUtils class.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that {@link CharSequenceUtils#lastIndexOf(CharSequence, CharSequence, int)}
     * returns -1 when searching for a non-empty sequence within an empty sequence.
     */
    @Test
    public void testLastIndexOf_inEmptySequence_shouldReturnNotFound() {
        // Arrange: Define the test data
        final CharSequence emptySequence = new StringBuilder();
        final CharSequence searchSequence = "a";
        // The start index is arbitrary since the sequence being searched is empty.
        final int startIndex = 5;

        // Act: Call the method under test
        final int result = CharSequenceUtils.lastIndexOf(emptySequence, searchSequence, startIndex);

        // Assert: Verify the outcome
        final int expected = -1; // The constant for "not found"
        assertEquals("Searching in an empty sequence should always return -1.", expected, result);
    }
}