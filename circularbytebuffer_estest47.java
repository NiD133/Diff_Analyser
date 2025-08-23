package org.apache.commons.io.input.buffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Tests for {@link CircularByteBuffer}.
 */
public class CircularByteBufferTest {

    /**
     * Tests that attempting to add a byte to a buffer with zero capacity
     * (which is inherently full) throws an {@link IllegalStateException}.
     */
    @Test
    public void addByteToFullBufferThrowsIllegalStateException() {
        // Arrange: Create a buffer with zero capacity, making it instantly full.
        final CircularByteBuffer zeroCapacityBuffer = new CircularByteBuffer(0);

        // Act & Assert: Verify that adding a byte throws an IllegalStateException
        // with the expected message "No space available".
        final IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            zeroCapacityBuffer.add((byte) 1);
        });

        assertEquals("No space available", thrown.getMessage());
    }
}