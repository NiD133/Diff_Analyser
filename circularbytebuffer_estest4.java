package org.apache.commons.io.input.buffer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link CircularByteBuffer}.
 */
public class CircularByteBufferTest {

    @Test
    public void readShouldThrowIllegalArgumentExceptionWhenOffsetIsOutsideTargetBounds() {
        // Arrange
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] destination = new byte[1];
        final int invalidOffset = 1; // An offset of 1 is out of bounds for an array of length 1 (valid indices are 0).
        final int length = 1;

        // Act & Assert
        try {
            buffer.read(destination, invalidOffset, length);
            fail("Expected an IllegalArgumentException because the offset is out of bounds for the destination array.");
        } catch (final IllegalArgumentException e) {
            // The exception is expected.
            // We verify the message to ensure the correct validation failed.
            assertEquals("Illegal offset: 1", e.getMessage());
        }
    }
}