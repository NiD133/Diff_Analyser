package org.apache.commons.compress.utils;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 * Tests for the {@link ByteUtils} class.
 */
public class ByteUtilsTest {

    /**
     * Tests that {@code toLittleEndian} correctly writes a single-byte
     * representation of a long value into a byte array at a specified offset.
     */
    @Test
    public void toLittleEndianShouldWriteSingleByteValueAtGivenOffset() {
        // Arrange: Set up the initial state and expected outcome.
        final byte[] buffer = new byte[2]; // Initial state: {0, 0}
        final long valueToWrite = -30L;
        final int offset = 1;
        final int length = 1;

        final byte[] expectedBuffer = new byte[] { 0, (byte) -30 };

        // Act: Call the method under test.
        ByteUtils.toLittleEndian(buffer, valueToWrite, offset, length);

        // Assert: Verify the buffer was modified as expected.
        assertArrayEquals("The buffer should be modified at the correct offset.", expectedBuffer, buffer);
    }
}