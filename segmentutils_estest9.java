package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;

/**
 * Unit tests for the {@link SegmentUtils} class.
 */
public class SegmentUtilsTest {

    /**
     * Verifies that the {@code countBit16(int[])} method throws a
     * {@code NullPointerException} when given a null array as input.
     * This ensures the method correctly handles invalid arguments.
     */
    @Test(expected = NullPointerException.class)
    public void countBit16ForIntArrayShouldThrowNPEForNullInput() {
        // Calling the method with a null array is expected to fail fast.
        SegmentUtils.countBit16((int[]) null);
    }
}