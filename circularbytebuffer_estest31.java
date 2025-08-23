package org.apache.commons.io.input.buffer;

import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * Unit tests for {@link CircularByteBuffer}.
 */
public class CircularByteBufferTest {

    /**
     * Tests that the {@code peek} method throws an {@link IllegalArgumentException}
     * when the requested number of bytes to read would exceed the capacity of the
     * destination buffer, considering the given offset.
     */
    @Test
    public void peekShouldThrowIllegalArgumentExceptionWhenReadExceedsTargetBufferCapacity() {
        // Arrange
        // The buffer's size and content are irrelevant for this test, as the
        // exception is thrown due to invalid arguments before any peeking occurs.
        final CircularByteBuffer buffer = new CircularByteBuffer(1);
        final byte[] destinationBuffer = new byte[10];

        // Act: Attempt to peek a number of bytes that, combined with the offset,
        // would write past the end of the destination buffer.
        final int offset = 5;
        final int length = 6; // 5 (offset) + 6 (length) > 10 (destinationBuffer.length)

        // Assert
        try {
            buffer.peek(destinationBuffer, offset, length);
            fail("Expected an IllegalArgumentException to be thrown because the combination of offset and length is out of bounds for the destination buffer.");
        } catch (final IllegalArgumentException e) {
            // This is the expected behavior. A more specific test could also assert
            // the content of the exception message if it's part of the API contract.
        }
    }
}