package org.apache.commons.lang3.math;

import org.junit.Test;

/**
 * Unit tests for the {@link IEEE754rUtils} class.
 */
public class IEEE754rUtilsTest {

    /**
     * Tests that the max(double...) method throws a NullPointerException when the input array is null,
     * as specified by its contract.
     */
    @Test(expected = NullPointerException.class)
    public void max_withNullDoubleArray_shouldThrowNullPointerException() {
        // The method under test requires a non-null array.
        // This call is expected to fail with a NullPointerException.
        IEEE754rUtils.max((double[]) null);
    }
}