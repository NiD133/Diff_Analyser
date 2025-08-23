package org.apache.commons.io.input.buffer;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link CircularByteBuffer}.
 */
public class CircularByteBufferTest {

    /**
     * Tests that the buffer's state (current size and available space) is correctly
     * updated after being completely filled and then completely emptied.
     */
    @Test
    public void addThenRead_whenBufferIsCompletelyFilledAndEmptied_updatesStateCorrectly() {
        // Arrange: Create a small buffer and a data array for transfers.
        final int bufferSize = 2;
        final int dataOffset = 2; // Arbitrary offset into the data array.
        final int length = 2;     // Number of bytes to transfer, same as buffer size.

        final CircularByteBuffer buffer = new CircularByteBuffer(bufferSize);
        final byte[] data = new byte[10]; // A source/destination array for the data.

        // Act 1: Fill the buffer completely.
        buffer.add(data, dataOffset, length);

        // Assert 1: Verify the buffer is full.
        assertEquals("Buffer should contain the number of bytes added.", bufferSize, buffer.getCurrentNumberOfBytes());
        assertFalse("A full buffer should not have space.", buffer.hasSpace());

        // Act 2: Empty the buffer completely.
        buffer.read(data, dataOffset, length);

        // Assert 2: Verify the buffer is empty.
        assertEquals("Buffer should be empty after reading all bytes.", 0, buffer.getCurrentNumberOfBytes());
        assertTrue("An empty buffer should have space.", buffer.hasSpace());
    }
}