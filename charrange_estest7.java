package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link org.apache.commons.lang3.CharRange}.
 */
public class CharRangeTest {

    /**
     * Tests that the CharRange.isNot() factory method correctly creates a
     * negated range for a single character.
     */
    @Test
    public void isNot_shouldCreateNegatedRangeForSingleCharacter() {
        // Arrange
        final char testChar = 'k';

        // Act: Create a negated range for a single character.
        final CharRange negatedRange = CharRange.isNot(testChar);

        // Assert: Verify the properties of the created range.
        // It should be marked as negated, and its start and end should be the specified character.
        assertTrue("The range should be negated.", negatedRange.isNegated());
        assertEquals("The start character should match the input.", testChar, negatedRange.getStart());
        assertEquals("The end character should match the input.", testChar, negatedRange.getEnd());
    }
}