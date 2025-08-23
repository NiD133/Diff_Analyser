package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link CharRange}.
 */
public class CharRangeTest {

    /**
     * Tests that getStart() returns the correct character for a negated range
     * created with the isNotIn() factory method.
     */
    @Test
    public void getStartShouldReturnStartCharForNegatedRange() {
        // Arrange: Create a negated range for a single character 'N'.
        // This range represents all characters EXCEPT 'N'.
        final char rangeChar = 'N';
        final CharRange negatedRange = CharRange.isNotIn(rangeChar, rangeChar);

        // Act: Get the start character from the range.
        final char actualStart = negatedRange.getStart();

        // Assert: Verify the properties of the range and the result of getStart().
        // The start and end boundaries should still be 'N', even though the range is negated.
        assertTrue("The range should be marked as negated.", negatedRange.isNegated());
        assertEquals("The start character of the range definition should be correct.", rangeChar, actualStart);
        assertEquals("The end character of the range definition should be correct.", rangeChar, negatedRange.getEnd());
    }
}