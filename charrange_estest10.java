package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * A test suite for the {@link CharRange} class.
 * This focuses on testing the factory methods and property accessors.
 */
public class CharRangeTest {

    /**
     * Tests that {@link CharRange#is(char)} correctly creates a non-negated range
     * for a single character, where the start and end characters are identical.
     */
    @Test
    public void testIsCreatesCorrectRangeForSingleCharacter() {
        // Arrange: Define the single character for the range.
        final char testChar = '/';

        // Act: Create the CharRange using the 'is' factory method.
        final CharRange range = CharRange.is(testChar);

        // Assert: Verify the properties of the created range.
        assertFalse("The range should not be negated.", range.isNegated());
        assertEquals("The start character should match the input character.", testChar, range.getStart());
        assertEquals("The end character should match the input character.", testChar, range.getEnd());
    }
}