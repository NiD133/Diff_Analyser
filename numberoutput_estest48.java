package com.fasterxml.jackson.core.io;

import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link NumberOutput} class, focusing on exception handling.
 */
public class NumberOutputTest {

    /**
     * Verifies that outputLong() throws an ArrayIndexOutOfBoundsException when the
     * provided byte array buffer is too small to hold the string representation of the long value.
     */
    @Test
    public void outputLongShouldThrowExceptionWhenBufferIsTooSmall() {
        // Arrange: A long value that requires 18 bytes to represent as a string.
        long largeValue = 407030665140201709L; // This number has 18 digits.

        // Create a buffer that is intentionally too small to hold the number.
        final int insufficientBufferSize = 14;
        byte[] buffer = new byte[insufficientBufferSize];
        int offset = 0;

        // Act & Assert
        try {
            NumberOutput.outputLong(largeValue, buffer, offset);
            fail("Expected an ArrayIndexOutOfBoundsException because the buffer is too small.");
        } catch (ArrayIndexOutOfBoundsException e) {
            // This is the expected outcome. The test passes if this exception is caught.
            // No further assertions are needed as the exception type itself is the verification.
        }
    }
}