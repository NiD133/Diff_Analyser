package org.apache.commons.io.input.buffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the clear() method in {@link CircularByteBuffer}.
 */
class CircularByteBufferTest {

    private static final int BUFFER_SIZE = 10;

    @Test
    @DisplayName("clear() should reset the buffer to its initial empty state")
    void clearShouldResetBufferToEmptyState() {
        // Arrange: Create a buffer and add some data to it.
        final byte[] initialData = { 1, 2, 3 };
        final CircularByteBuffer buffer = new CircularByteBuffer(BUFFER_SIZE);
        buffer.add(initialData, 0, initialData.length);

        // Sanity check to ensure the buffer is not empty before clearing.
        assertTrue(buffer.hasBytes(), "Precondition failed: Buffer should contain bytes before clearing.");
        assertEquals(initialData.length, buffer.getCurrentNumberOfBytes(), "Precondition failed: Incorrect byte count before clearing.");

        // Act: Call the method under test.
        buffer.clear();

        // Assert: Verify the buffer is now empty and has its full capacity available.
        assertFalse(buffer.hasBytes(), "Buffer should be empty after clear().");
        assertEquals(0, buffer.getCurrentNumberOfBytes(), "Current number of bytes should be 0 after clear().");
        
        assertTrue(buffer.hasSpace(), "Buffer should have space available after clear().");
        assertEquals(BUFFER_SIZE, buffer.getSpace(), "Available space should be reset to the total buffer size.");
    }
}