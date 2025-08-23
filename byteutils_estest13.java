package org.apache.commons.compress.utils;

import org.junit.Test;

/**
 * Unit tests for the {@link ByteUtils} class.
 */
public class ByteUtilsTest {

    /**
     * Tests that {@link ByteUtils#toLittleEndian(byte[], long, int, int)} throws an
     * ArrayIndexOutOfBoundsException if the combination of offset and length
     * exceeds the bounds of the destination byte array.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void toLittleEndianShouldThrowExceptionWhenWriteExceedsBufferBounds() {
        // Arrange: A buffer that is too small for the requested write operation.
        final byte[] buffer = new byte[3];
        final long valueToWrite = 1L;
        final int offset = 1;
        // The length to write (63) plus the offset (1) requires a buffer of at least size 64.
        final int length = 63;

        // Act: Attempt to write past the end of the buffer.
        // Assert: An ArrayIndexOutOfBoundsException is expected, declared via the @Test annotation.
        ByteUtils.toLittleEndian(buffer, valueToWrite, offset, length);
    }
}