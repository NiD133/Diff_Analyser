package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Tests for {@link CharRange}.
 */
public class CharRangeTest {

    @Test
    public void is_shouldCreateCorrectRangeForSingleCharacter() {
        // Arrange
        final char testChar = '4';

        // Act: Create a CharRange for a single character.
        final CharRange range = CharRange.is(testChar);

        // Assert: Verify the properties of the created range.
        // A single-character range should have the same start and end character
        // and should not be negated.
        assertEquals("The start character should match the input.", testChar, range.getStart());
        assertEquals("The end character should match the input.", testChar, range.getEnd());
        assertFalse("The range for a single character should not be negated.", range.isNegated());
    }
}