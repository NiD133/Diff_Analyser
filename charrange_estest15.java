package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for {@link CharRange}.
 */
public class CharRangeTest {

    /**
     * Tests that the {@code CharRange.is(char)} factory method correctly creates a
     * non-negated range representing a single character.
     */
    @Test
    public void testIsForSingleCharacter() {
        // Arrange: Define the character for the range.
        final char testChar = 'P';

        // Act: Create the CharRange using the factory method under test.
        final CharRange range = CharRange.is(testChar);

        // Assert: Verify the properties of the created range.
        assertEquals("The start character should be the input character.", testChar, range.getStart());
        assertEquals("The end character should be the input character.", testChar, range.getEnd());
        assertFalse("A range created with is() should not be negated.", range.isNegated());
        assertEquals("The string representation should be the character itself.", "P", range.toString());
    }
}