package org.apache.commons.compress.harmony.unpack200;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * This test suite contains tests for the {@link SegmentUtils} class.
 * This version improves upon an auto-generated test by using descriptive names,
 * clear inputs, and standard testing practices.
 */
public class SegmentUtilsTest {

    /**
     * Tests that {@code countBit16} correctly counts one element in an integer array
     * when that element has its 16th bit (bit index 15) set.
     */
    @Test
    public void countBit16ForIntArrayShouldReturnOneForSingleElementWith16thBitSet() {
        // Arrange: Create an integer array with a single element that has the 16th bit set.
        // Using a bit shift (1 << 15) makes the intent clear, unlike the original test's
        // magic number (-142). This value is 32768.
        final int valueWith16thBitSet = 1 << 15;
        final int[] flags = { valueWith16thBitSet };

        // Act: Call the method to count elements with the 16th bit set.
        final int count = SegmentUtils.countBit16(flags);

        // Assert: The result should be 1, as there is one matching element.
        assertEquals("The count of integers with the 16th bit set should be 1.", 1, count);
    }
}