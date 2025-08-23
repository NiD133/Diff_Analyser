package org.apache.commons.io.input.buffer;

import org.junit.Test;

/**
 * Unit tests for the {@link CircularByteBuffer} class, focusing on exception-throwing behavior.
 */
public class CircularByteBufferTest {

    /**
     * Verifies that the peek() method correctly throws an IllegalArgumentException
     * when provided with a negative length argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void peekShouldThrowIllegalArgumentExceptionWhenLengthIsNegative() {
        // Arrange: Create a buffer and define the invalid arguments for the peek method.
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] anyTargetBuffer = new byte[10];
        final int anyOffset = 0;
        final int negativeLength = -1;

        // Act: Call the peek method with a negative length.
        // This action is expected to throw the exception.
        buffer.peek(anyTargetBuffer, anyOffset, negativeLength);

        // Assert: The test passes if an IllegalArgumentException is thrown,
        // as specified by the @Test(expected=...) annotation.
    }
}