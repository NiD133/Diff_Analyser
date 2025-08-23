package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link ByteArrayBuilder} class, focusing on its buffer
 * management and writing capabilities.
 */
public class ByteArrayBuilderTest {

    /**
     * Tests that `appendFourBytes` correctly allocates a new buffer segment
     * when the current segment does not have enough space.
     */
    @Test
    public void appendFourBytes_whenCurrentSegmentIsFull_allocatesNewSegment() {
        // Arrange: Create a builder with a small buffer that has less than 4 bytes of free space.
        // A buffer of size 5 is created, and we write 3 bytes to it, leaving 2 bytes free.
        final int initialCapacity = 5;
        ByteArrayBuilder builder = new ByteArrayBuilder(initialCapacity);
        builder.write(new byte[3]); // Fill 3 bytes, leaving 2 bytes of space.

        // Pre-condition check to ensure our setup is correct.
        assertEquals("Pre-condition failed: Initial total size should be 3", 3, builder.size());
        assertEquals("Pre-condition failed: Initial segment should have 3 bytes", 3, builder.getCurrentSegmentLength());

        // Act: Append four bytes. This should not fit in the current segment and
        // should trigger the allocation of a new one.
        int valueToAppend = 0x1A2B3C4D;
        builder.appendFourBytes(valueToAppend);

        // Assert: Verify the state after appending.
        // 1. The total size should be the initial 3 bytes plus the 4 appended bytes.
        assertEquals("Total size should be 7 after appending four bytes", 7, builder.size());

        // 2. The new current segment should contain exactly the 4 bytes we just appended.
        assertEquals("New current segment should contain 4 bytes", 4, builder.getCurrentSegmentLength());

        // 3. The entire content of the builder should be correct.
        byte[] expectedBytes = new byte[] {
            0, 0, 0, // The initial 3 bytes
            (byte) 0x1A, (byte) 0x2B, (byte) 0x3C, (byte) 0x4D // The appended 4 bytes
        };
        assertArrayEquals("The final byte array content should be correct", expectedBytes, builder.toByteArray());
    }
}