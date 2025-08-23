package org.apache.commons.io.input.buffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Unit tests for {@link CircularByteBuffer}.
 */
public class CircularByteBufferTest {

    @Test
    public void addWithOutOfBoundsOffsetThrowsIllegalArgumentException() {
        // Arrange: Create a buffer and a source byte array.
        // The offset is intentionally set to a value outside the bounds of the source array.
        final CircularByteBuffer buffer = new CircularByteBuffer(0);
        final byte[] sourceData = new byte[3];
        final int invalidOffset = 1886;
        final int length = 0; // Adding zero bytes, so buffer capacity is not a factor.

        // Act & Assert: Verify that calling add() with an invalid offset throws an exception.
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            buffer.add(sourceData, invalidOffset, length);
        });

        // Further Assert: Check if the exception message is as expected, confirming the reason for failure.
        assertEquals("Illegal offset: " + invalidOffset, exception.getMessage());
    }
}