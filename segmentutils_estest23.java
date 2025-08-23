package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link SegmentUtils}.
 */
public class SegmentUtilsTest {

    private static final long FLAG_WITH_BIT_16_SET = 1L << 16; // Represents 65536, or 0x10000
    private static final long FLAG_WITHOUT_BIT_16_SET = (1L << 16) - 1; // Represents 65535, or 0xFFFF

    @Test
    public void countBit16ShouldReturnOneForArrayWithSingleMatchingFlag() {
        // Arrange: Create an array of flags where only one element has the 16th bit set.
        // We include other values to ensure they are correctly ignored.
        long[] flags = { 0L, 123L, FLAG_WITH_BIT_16_SET, -1L, FLAG_WITHOUT_BIT_16_SET };

        // Act: Call the method to count the flags with the 16th bit set.
        int count = SegmentUtils.countBit16(flags);

        // Assert: The count should be exactly 1.
        assertEquals(1, count);
    }

    @Test
    public void countBit16ShouldReturnZeroForArrayWithNoMatchingFlags() {
        // Arrange: Create an array where no elements have the 16th bit set.
        long[] flags = { 0L, 123L, -1L, FLAG_WITHOUT_BIT_16_SET };

        // Act
        int count = SegmentUtils.countBit16(flags);

        // Assert
        assertEquals(0, count);
    }

    @Test
    public void countBit16ShouldReturnCorrectCountForArrayWithMultipleMatchingFlags() {
        // Arrange: Create an array with multiple elements having the 16th bit set.
        long[] flags = { FLAG_WITH_BIT_16_SET, 1L, FLAG_WITH_BIT_16_SET | 255L, FLAG_WITH_BIT_16_SET | -1L };

        // Act
        int count = SegmentUtils.countBit16(flags);

        // Assert
        assertEquals(3, count);
    }
}