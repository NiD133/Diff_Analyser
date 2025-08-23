package org.apache.commons.io.input.buffer;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the CircularByteBuffer class.
 */
public class CircularByteBufferTest {

    /**
     * Tests that calling read() on an empty buffer throws an IllegalStateException
     * when attempting to read more bytes than are available.
     */
    @Test
    public void readShouldThrowIllegalStateExceptionWhenBufferIsEmpty() {
        // Arrange: Create an empty buffer and a destination for the read operation.
        final byte[] destinationBuffer = new byte[10];
        final CircularByteBuffer circularByteBuffer = new CircularByteBuffer(8); // Buffer with capacity, but currently empty.
        final int bytesToRead = 2;
        final int destinationOffset = 2;

        // Act & Assert: Attempting to read from the empty buffer should throw an exception.
        try {
            circularByteBuffer.read(destinationBuffer, destinationOffset, bytesToRead);
            fail("Expected an IllegalStateException to be thrown because the buffer is empty.");
        } catch (final IllegalStateException e) {
            // Verify that the exception message correctly identifies the issue.
            final String expectedMessage = "Currently, there are only 0 in the buffer, not 2";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}