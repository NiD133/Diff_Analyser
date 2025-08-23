package org.apache.commons.lang3.math;

import org.junit.Test;

/**
 * Unit tests for {@link IEEE754rUtils}.
 */
public class IEEE754rUtilsTest {

    /**
     * Tests that the min(double...) method throws a NullPointerException when
     * the input array is null, as specified by its contract.
     */
    @Test(expected = NullPointerException.class)
    public void minDoubleArray_shouldThrowNullPointerException_forNullInput() {
        // The varargs method is equivalent to a method accepting an array.
        // We pass a null array to trigger the expected exception.
        IEEE754rUtils.min((double[]) null);
    }
}