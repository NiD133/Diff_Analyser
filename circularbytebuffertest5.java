package org.apache.commons.io.input.buffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CircularByteBuffer}.
 */
class CircularByteBufferTest {

    /**
     * Tests that adding a byte array to the buffer correctly stores the bytes
     * and updates the internal byte count.
     */
    @Test
    void addByteArrayShouldStoreBytesAndIncreaseCount() {
        // Arrange: Create a buffer and the data to be added.
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] dataToAdd = {3, 6, 9};
        final int offset = 0;
        final int length = dataToAdd.length;

        // Act: Add the byte array segment to the buffer.
        buffer.add(dataToAdd, offset, length);

        // Assert: Verify the buffer's state is correct.
        assertEquals(length, buffer.getCurrentNumberOfBytes(),
            "The number of bytes in the buffer should match the length of the data added.");

        // Verify the content of the buffer without consuming the bytes.
        assertTrue(buffer.peek(dataToAdd, offset, length),
            "The buffer should contain the exact bytes that were added.");
    }
}