package org.apache.commons.io.input.buffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

/**
 * Tests for {@link CircularByteBuffer}.
 */
public class CircularByteBufferTest {

    /**
     * Tests that adding a single byte and then reading it returns the correct byte,
     * leaving the buffer empty.
     */
    @Test
    public void addThenReadSingleByteShouldReturnSameByteAndEmptyBuffer() {
        // Arrange: Create a buffer with the default size and define a byte to test.
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte byteToAdd = (byte) 123;
        final int initialSpace = IOUtils.DEFAULT_BUFFER_SIZE;

        // Act: Add the byte to the buffer and then immediately read it back.
        buffer.add(byteToAdd);
        final byte readByte = buffer.read();

        // Assert: Verify the outcome.
        assertEquals("The byte read should be the same as the byte added.", byteToAdd, readByte);
        
        // Verify the buffer is empty again.
        assertEquals("Buffer should have no bytes after reading the only element.", 0, buffer.getCurrentNumberOfBytes());
        assertFalse("Buffer should not have any bytes available.", buffer.hasBytes());
        assertEquals("Buffer space should be fully restored to its initial capacity.", initialSpace, buffer.getSpace());
    }
}