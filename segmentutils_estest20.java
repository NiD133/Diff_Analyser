package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test suite verifies the behavior of the {@link SegmentUtils} class.
 */
public class SegmentUtilsTest {

    /**
     * Tests that {@code countMatches} correctly returns 0 when given an empty array.
     * This is a fundamental edge case to ensure the method handles empty inputs gracefully.
     */
    @Test
    public void countMatchesShouldReturnZeroForEmptyArray() {
        // Arrange: Define an empty array of flags. The matcher is null because it will
        // not be used when iterating over an empty array.
        final long[] emptyFlags = new long[0];
        final IMatcher matcher = null;

        // Act: Call the method under test with the empty array.
        final int matchCount = SegmentUtils.countMatches(emptyFlags, matcher);

        // Assert: Verify that the number of matches is zero.
        assertEquals("The count of matches in an empty array should be zero.", 0, matchCount);
    }
}