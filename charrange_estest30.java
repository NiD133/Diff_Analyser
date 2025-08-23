package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link CharRange} class.
 */
public class CharRangeTest {

    /**
     * Tests that a CharRange instance is correctly identified as containing itself.
     */
    @Test
    public void testContains_withSelf_shouldReturnTrue() {
        // Arrange: Create a character range.
        final CharRange range = CharRange.isIn('8', 'A');

        // Act: Check if the range contains itself.
        final boolean result = range.contains(range);

        // Assert: The result should be true, as a range always contains itself.
        assertTrue("A range should always contain itself.", result);

        // Additionally, verify the range was constructed as expected to ensure the test setup is valid.
        assertEquals("The start of the range should be '8'.", '8', range.getStart());
        assertEquals("The end of the range should be 'A'.", 'A', range.getEnd());
    }
}