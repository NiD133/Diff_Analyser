package org.apache.commons.io.input.buffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Tests for {@link CircularByteBuffer}.
 */
public class CircularByteBufferTest {

    /**
     * Tests that {@link CircularByteBuffer#peek(byte[], int, int)} throws an
     * {@link IllegalArgumentException} if the specified offset is outside the
     * bounds of the source byte array.
     */
    @Test
    public void peekShouldThrowExceptionForOffsetOutOfBounds() {
        // Arrange: Set up the test conditions.
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] sourceBuffer = new byte[4];
        final int invalidOffset = 7; // An offset greater than the source buffer's length.
        final int length = 1;        // A valid length to isolate the offset as the cause of the error.

        // Act & Assert: Execute the method and verify the outcome.
        try {
            buffer.peek(sourceBuffer, invalidOffset, length);
            fail("Expected an IllegalArgumentException because the offset is out of bounds.");
        } catch (final IllegalArgumentException e) {
            // The exception is expected.
            // Verify that the exception message clearly indicates the problem.
            final String expectedMessage = "Illegal offset: " + invalidOffset;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}