package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * This class contains tests for the {@link CharRange} class.
 * This specific test focuses on the {@code contains} method.
 */
// The original test class name and hierarchy are preserved to show a direct comparison.
public class CharRange_ESTestTest33 extends CharRange_ESTest_scaffolding {

    /**
     * Verifies that the contains() method returns false for a character
     * that is numerically smaller than the start of the range.
     */
    @Test
    public void containsShouldReturnFalseForCharOutsideAndBelowRange() {
        // Arrange: Create a character range from '8' to 'A'.
        // The character to test, a space (' '), is outside this range.
        final CharRange range = CharRange.isIn('8', 'A');
        final char charBelowRange = ' '; // ASCII 32 is less than '8' (ASCII 56)

        // Act: Check if the character is contained within the range.
        final boolean isContained = range.contains(charBelowRange);

        // Assert: The character should not be in the range.
        assertFalse("Character ' ' should be outside the range '8'-'A'", isContained);

        // Also, verify the range was constructed as expected to ensure the test is valid.
        assertEquals("The start of the range should be '8'", '8', range.getStart());
        assertEquals("The end of the range should be 'A'", 'A', range.getEnd());
    }
}