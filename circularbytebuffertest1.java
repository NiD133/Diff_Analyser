package org.apache.commons.io.input.buffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CircularByteBuffer}.
 */
class CircularByteBufferTest {

    @Test
    @DisplayName("A buffer with single-byte capacity should successfully reuse its space after a read operation")
    void singleByteCapacityBufferShouldAllowAddingAfterReading() {
        // Arrange: Create a buffer with the smallest possible capacity (1 byte).
        final CircularByteBuffer buffer = new CircularByteBuffer(1);
        assertTrue(buffer.hasSpace(), "A new buffer should have space.");
        assertFalse(buffer.hasBytes(), "A new buffer should be empty.");

        // Act 1: Add a byte, which should fill the buffer.
        buffer.add((byte) 1);

        // Assert 1: The buffer is full and contains the added byte.
        assertFalse(buffer.hasSpace(), "Buffer should be full after adding one byte.");
        assertEquals(1, buffer.getCurrentNumberOfBytes(), "Buffer should contain one byte.");
        
        // Act 2: Read the byte from the buffer.
        final byte readByte1 = buffer.read();

        // Assert 2: The correct byte was read, and the buffer is now empty again.
        assertEquals((byte) 1, readByte1, "The byte read should match the byte added.");
        assertTrue(buffer.hasSpace(), "Space should be available again after a read.");
        assertFalse(buffer.hasBytes(), "Buffer should be empty after a read.");

        // Act 3: Add a second byte to test if the space was correctly reused.
        buffer.add((byte) 2);

        // Assert 3: The buffer is full again with the new byte.
        assertFalse(buffer.hasSpace(), "Buffer should be full again after the second add.");
        final byte readByte2 = buffer.read();
        assertEquals((byte) 2, readByte2, "The second byte read should match the second byte added.");
    }
}