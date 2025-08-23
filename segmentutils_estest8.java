package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;

/**
 * This class contains tests for the {@link SegmentUtils} utility class.
 * This specific test case focuses on the behavior of the countBit16 method with long arrays.
 */
public class SegmentUtilsTest {

    /**
     * Verifies that the {@code countBit16(long[])} method throws a
     * {@code NullPointerException} when the input array is null.
     */
    @Test(expected = NullPointerException.class)
    public void countBit16WithLongArrayShouldThrowNullPointerExceptionForNullInput() {
        // The method under test should not accept a null array.
        SegmentUtils.countBit16((long[]) null);
    }
}