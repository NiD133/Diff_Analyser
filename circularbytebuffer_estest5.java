package org.apache.commons.io.input.buffer;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link CircularByteBuffer} class.
 */
public class CircularByteBufferTest {

    /**
     * Tests that reading a byte from a buffer with multiple bytes returns the
     * first byte that was added (FIFO) and leaves the remaining bytes in the buffer.
     */
    @Test
    public void readShouldReturnFirstByteAndLeaveRemainingBytes() {
        // Arrange: Create a buffer and add two distinct bytes.
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte firstByteAdded = 10;
        final byte secondByteAdded = 20;
        buffer.add(firstByteAdded);
        buffer.add(secondByteAdded);

        // Act: Read one byte from the buffer.
        final byte actualReadByte = buffer.read();

        // Assert: Verify that the correct byte was read and the buffer's state is updated as expected.
        assertEquals("The read byte should be the first one that was added.", firstByteAdded, actualReadByte);
        assertTrue("Buffer should still contain bytes after reading one of two.", buffer.hasBytes());
        assertEquals("Buffer should have exactly one byte remaining.", 1, buffer.getCurrentNumberOfBytes());
    }
}