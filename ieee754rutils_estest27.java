package org.apache.commons.lang3.math;

import org.junit.Test;

/**
 * Unit tests for the {@link IEEE754rUtils} class.
 */
public class IEEE754rUtilsTest {

    /**
     * Tests that calling max() with a null float array throws a NullPointerException.
     * This verifies the contract that the input array must not be null.
     */
    @Test(expected = NullPointerException.class)
    public void maxFloatArray_withNullArray_shouldThrowNullPointerException() {
        // When the max method is called with a null float array
        IEEE754rUtils.max((float[]) null);

        // Then a NullPointerException is expected to be thrown.
        // This is asserted by the 'expected' attribute of the @Test annotation.
    }
}