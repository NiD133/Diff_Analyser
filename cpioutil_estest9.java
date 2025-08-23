package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Unit tests for the CpioUtil class.
 */
public class CpioUtilTest {

    /**
     * Tests that long2byteArray throws an UnsupportedOperationException when the
     * requested byte array length is zero. The method's contract requires a
     * positive length.
     */
    @Test
    public void long2byteArrayShouldThrowExceptionForZeroLength() {
        // Arrange: The number and swapHalfWord parameters are arbitrary, as the
        // exception is triggered by the invalid length.
        final long anyNumber = 3061L;
        final int invalidLength = 0;
        final boolean swapHalfWord = true;

        // Act & Assert
        try {
            CpioUtil.long2byteArray(anyNumber, invalidLength, swapHalfWord);
            fail("Expected an UnsupportedOperationException for a non-positive length.");
        } catch (final UnsupportedOperationException e) {
            // This is the expected behavior.
            // The original test also verified that the exception has no message.
            assertNull("The exception message was expected to be null.", e.getMessage());
        }
    }
}