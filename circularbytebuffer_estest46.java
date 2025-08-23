package org.apache.commons.io.input.buffer;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link CircularByteBuffer} class.
 */
public class CircularByteBufferTest {

    /**
     * Tests that adding a byte to a full buffer and then reading it
     * correctly updates the available space and returns the correct byte.
     */
    @Test
    public void addAndReadSingleByteShouldUpdateSpaceAndReturnCorrectValue() {
        // Arrange: Create a buffer with a capacity of exactly one byte.
        final CircularByteBuffer buffer = new CircularByteBuffer(1);
        final byte byteToWrite = 42;

        // Act 1: Add a byte to fill the buffer.
        buffer.add(byteToWrite);

        // Assert 1: Verify the buffer is full.
        assertEquals("After adding one byte, the buffer should be full.", 0, buffer.getSpace());

        // Act 2: Read the byte from the buffer.
        final byte readByte = buffer.read();

        // Assert 2: Verify the buffer is empty again and the correct byte was read.
        assertEquals("After reading the byte, the buffer should have space for one byte.", 1, buffer.getSpace());
        assertEquals("The byte read from the buffer should match the byte that was written.", byteToWrite, readByte);
    }
}