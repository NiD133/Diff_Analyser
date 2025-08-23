package org.apache.commons.compress.archivers.cpio;

import org.junit.Test;

/**
 * Unit tests for the {@link CpioUtil} class.
 */
public class CpioUtilTest {

    /**
     * Verifies that long2byteArray throws an UnsupportedOperationException
     * when the provided length is not a multiple of two, as required by the CPIO format.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void long2byteArrayShouldThrowExceptionForInvalidLength() {
        // Arrange: Define an invalid length that is not a multiple of two.
        // The CPIO specification requires field lengths to be multiples of two.
        final int invalidOddLength = 701;
        
        // The actual number and the swap flag are irrelevant for this specific exception.
        final long anyNumber = 12345L;
        final boolean swapHalfWord = false;

        // Act: Attempt to convert a long to a byte array with an invalid length.
        // Assert: The @Test(expected=...) annotation handles the assertion,
        // ensuring an UnsupportedOperationException is thrown.
        CpioUtil.long2byteArray(anyNumber, invalidOddLength, swapHalfWord);
    }
}