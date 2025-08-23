package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link CharRange} class.
 */
public class CharRangeTest {

    /**
     * Tests that the {@code CharRange.isIn(start, end)} factory method correctly
     * creates a range by swapping the arguments if the start character is greater
     * than the end character.
     */
    @Test
    public void isInShouldCreateCorrectRangeWhenStartIsGreaterThanEnd() {
        // Arrange: Define the expected start and end characters.
        // Note that they are passed to the factory method in reverse order.
        final char expectedStart = '#';
        final char expectedEnd = 'K';

        // Act: Create a CharRange using the isIn factory method with out-of-order arguments.
        // The source code documentation states that start and end are reversed if out of order.
        final CharRange range = CharRange.isIn(expectedEnd, expectedStart);

        // Assert: Verify that the range was created with the correct start and end points
        // and that its contains() method works as expected for a boundary value.
        assertEquals("The start character should be the smaller of the two inputs.", expectedStart, range.getStart());
        assertEquals("The end character should be the larger of the two inputs.", expectedEnd, range.getEnd());
        assertTrue("The range should contain its start character.", range.contains(expectedStart));
    }
}