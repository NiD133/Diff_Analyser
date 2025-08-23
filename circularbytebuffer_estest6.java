package org.apache.commons.io.input.buffer;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Tests for the {@link CircularByteBuffer} class.
 */
public class CircularByteBufferTest {

    @Test
    public void peekShouldReturnFalseIfBufferHasInsufficientBytes() {
        // Arrange: Create an empty buffer, which has 0 bytes available.
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final int lengthToPeek = 25;
        final byte[] comparisonBytes = new byte[lengthToPeek];

        // The buffer is empty, so it has fewer bytes than we want to peek.

        // Act: Attempt to peek for more bytes than are available.
        final boolean result = buffer.peek(comparisonBytes, 0, lengthToPeek);

        // Assert: The operation should fail and not alter the buffer's state.
        assertFalse("peek() should return false when the buffer contains fewer bytes than requested.", result);
        
        // Also, verify that this non-destructive operation did not change the buffer's state.
        assertEquals("The number of bytes in the buffer should remain 0.", 0, buffer.getCurrentNumberOfBytes());
        assertEquals("The available space should still be the initial capacity.", IOUtils.DEFAULT_BUFFER_SIZE, buffer.getSpace());
    }
}