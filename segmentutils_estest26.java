package org.apache.commons.compress.harmony.unpack200;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests for {@link SegmentUtils}.
 */
public class SegmentUtilsTest {

    /**
     * Tests that {@code SegmentUtils.countBit16(int[])} correctly returns 0
     * when passed an array where no integer has the 16th bit set.
     */
    @Test
    public void countBit16ForIntArrayShouldReturnZeroWhenNoBitIsSet() {
        // Arrange: Create an array of flags where no element has the 16th bit set.
        // The value 0 is a simple and clear case for this.
        final int[] flags = {0};

        // Act: Call the method to count the flags with the 16th bit set.
        final int count = SegmentUtils.countBit16(flags);

        // Assert: The result should be 0, as no flags met the condition.
        assertEquals("The count of flags with bit 16 set should be zero", 0, count);
    }
}