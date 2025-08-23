package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link SegmentUtils} class.
 */
public class SegmentUtilsTest {

    /**
     * Tests that countBit16 returns 0 for a 2D array of longs
     * where no elements have the 16th bit set.
     * This test case specifically includes empty sub-arrays to ensure they are handled correctly.
     */
    @Test
    public void countBit16For2DArrayShouldReturnZeroWhenNoFlagsAreSet() {
        // --- Arrange ---
        // A 2D array where no element has the 16th bit set.
        // Using an array initializer makes the input data much clearer than programmatic construction.
        // The structure mirrors the original test's intent, including empty sub-arrays.
        final long[][] flags = {
            {0L, 0L}, // A sub-array with zero-value elements
            {},         // An empty sub-array
            {},
            {},
            {},
            {}
        };

        final int expectedCount = 0;

        // --- Act ---
        // Call the method under test to count the flags.
        final int actualCount = SegmentUtils.countBit16(flags);

        // --- Assert ---
        // The count should be zero because no flags were set.
        assertEquals("The count of flags with the 16th bit set should be 0.", expectedCount, actualCount);
    }
}