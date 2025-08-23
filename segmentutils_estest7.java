package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;

/**
 * Tests for the {@link SegmentUtils} class.
 */
public class SegmentUtilsTest {

    /**
     * Verifies that countBit16 throws a NullPointerException when passed a null 2D array,
     * as the method cannot iterate over a null input.
     */
    @Test(expected = NullPointerException.class)
    public void countBit16ShouldThrowNullPointerExceptionForNull2DArray() {
        SegmentUtils.countBit16((long[][]) null);
    }
}