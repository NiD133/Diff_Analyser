package org.apache.commons.compress.archivers.cpio;

import org.junit.Test;

/**
 * Unit tests for the {@link CpioUtil} class.
 */
public class CpioUtilTest {

    /**
     * Tests that byteArray2long throws an UnsupportedOperationException
     * when the input byte array has a length that is not a multiple of two,
     * as specified by its contract.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void byteArray2longShouldThrowExceptionForOddLengthArray() {
        // Arrange: Create a byte array with an odd length (3).
        // The method documentation states it only accepts lengths that are multiples of 2.
        byte[] oddLengthInput = new byte[3];
        boolean swapHalfWord = true; // The value of this parameter is irrelevant for this test.

        // Act: Call the method with the invalid input.
        // Assert: The @Test(expected=...) annotation handles the exception assertion.
        CpioUtil.byteArray2long(oddLengthInput, swapHalfWord);
    }
}