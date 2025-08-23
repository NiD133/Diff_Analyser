package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for {@link CharSequenceUtils}.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that CharSequenceUtils.indexOf returns -1 when the search sequence is null,
     * aligning with the class's null-safe contract.
     */
    @Test
    public void indexOf_withNullSearchSequence_shouldReturnNotFound() {
        // Arrange: Define the input data for the test.
        // The content of the text to search is irrelevant when the search sequence is null.
        final CharSequence textToSearch = "any non-null string";
        final CharSequence nullSearchSequence = null;

        // Act: Execute the method under test.
        final int actualIndex = CharSequenceUtils.indexOf(textToSearch, nullSearchSequence, 0);

        // Assert: Verify the outcome.
        // The expected result is -1, which standardly indicates "not found".
        assertEquals(-1, actualIndex);
    }
}