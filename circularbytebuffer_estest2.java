package org.apache.commons.io.input.buffer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link CircularByteBuffer}.
 */
public class CircularByteBufferTest {

    /**
     * Tests that attempting to read from an empty buffer throws an IllegalStateException.
     */
    @Test
    public void read_whenBufferIsEmpty_throwsIllegalStateException() {
        // Arrange: Create an empty buffer and a destination array for the read operation.
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] destinationArray = new byte[4];
        final int offset = 3;
        final int lengthToRead = 1;

        // Act & Assert: Verify that calling read() on the empty buffer throws the expected exception.
        final IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            buffer.read(destinationArray, offset, lengthToRead);
        });

        // Assert: Check if the exception message is correct.
        final String expectedMessage = "Currently, there are only 0 in the buffer, not 1";
        assertEquals(expectedMessage, thrown.getMessage());
    }
}