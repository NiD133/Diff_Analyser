package org.apache.commons.io.input.buffer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link CircularByteBuffer} class.
 */
public class CircularByteBufferTest {

    /**
     * Tests that the read(byte[], int, int) method throws an IllegalArgumentException
     * when the provided offset is outside the bounds of the destination array.
     */
    @Test
    public void readShouldThrowIllegalArgumentExceptionForOutOfBoundsOffset() {
        // Arrange: Create a buffer and a destination array for the read operation.
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] destination = new byte[5];
        final int outOfBoundsOffset = 7; // An offset clearly larger than the destination array's length.
        final int length = 1;

        // Act & Assert: Attempt the read operation and verify the expected exception.
        try {
            buffer.read(destination, outOfBoundsOffset, length);
            fail("Expected an IllegalArgumentException because the offset is out of bounds.");
        } catch (final IllegalArgumentException e) {
            // Verify that the exception message is informative and correct.
            final String expectedMessage = "Illegal offset: " + outOfBoundsOffset;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}