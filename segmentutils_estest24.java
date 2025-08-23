package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link SegmentUtils} class.
 */
public class SegmentUtilsTest {

    /**
     * Tests that countBit16 returns 0 when given an array of longs where
     * no element has the 16th bit set. An array of all zeros is a perfect
     * example of this scenario.
     */
    @Test
    public void countBit16ShouldReturnZeroForArrayOfZeros() {
        // Arrange: Create an array of long values, which defaults to all elements being 0.
        long[] flagsWithNoBitsSet = new long[5];

        // Act: Call the method under test to count the occurrences of the 16th bit.
        int actualCount = SegmentUtils.countBit16(flagsWithNoBitsSet);

        // Assert: Verify that the count is 0, as expected.
        int expectedCount = 0;
        assertEquals("The count should be zero when no flags have the 16th bit set.", expectedCount, actualCount);
    }
}