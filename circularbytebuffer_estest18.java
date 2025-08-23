package org.apache.commons.io.input.buffer;

import org.junit.Test;

/**
 * Tests for {@link CircularByteBuffer}.
 */
public class CircularByteBufferTest {

    /**
     * Tests that peek() throws an ArrayIndexOutOfBoundsException when the provided
     * offset and length would cause an out-of-bounds access on the source buffer.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void peekShouldThrowExceptionForOffsetAndLengthExceedingSourceBounds() {
        // Arrange
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] sourceBuffer = new byte[22];
        final int offset = 16;
        final int length = 16;

        // The combination of offset and length (16 + 16 = 32) is out of bounds
        // for the sourceBuffer, which has a size of 22.

        // Act
        buffer.peek(sourceBuffer, offset, length);

        // Assert: The @Test(expected) annotation handles the exception assertion.
    }
}