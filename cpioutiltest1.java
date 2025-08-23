package org.apache.commons.compress.archivers.cpio;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link CpioUtil} class.
 */
class CpioUtilTest {

    @Test
    void byteArray2longShouldThrowExceptionForInputWithOddLength() {
        // Arrange: According to its Javadoc, byteArray2long requires the input
        // byte array's length to be a multiple of 2.
        // We create an array with an odd length (1) to test this constraint.
        final byte[] oddLengthInput = new byte[1];
        final boolean swapHalfWord = true; // The value is irrelevant for this exception case.

        // Act & Assert: Verify that the method throws an UnsupportedOperationException
        // when called with the odd-length array.
        assertThrows(UnsupportedOperationException.class, () -> {
            CpioUtil.byteArray2long(oddLengthInput, swapHalfWord);
        });
    }
}