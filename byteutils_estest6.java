package org.apache.commons.compress.utils;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 * Contains tests for the {@link ByteUtils} class.
 */
public class ByteUtilsTest {

    /**
     * Tests that toLittleEndian does not write to the buffer when the provided length is negative.
     * The internal loop for writing bytes should not execute, leaving the buffer unmodified.
     */
    @Test
    public void toLittleEndianWithNegativeLengthShouldNotModifyBuffer() {
        // Arrange
        // A buffer initialized with default zero values.
        byte[] buffer = new byte[4];
        // An identical buffer to verify that the original is not modified.
        byte[] expectedBuffer = new byte[4];

        final long valueToWrite = 0xDEADBEEFL; // An arbitrary non-zero value.
        final int offset = 0;                  // A valid offset.
        final int negativeLength = -1;         // The key condition being tested.

        // Act
        // Attempt to write to the buffer with a negative length.
        ByteUtils.toLittleEndian(buffer, valueToWrite, offset, negativeLength);

        // Assert
        // The buffer should remain unchanged because the negative length prevents any write operations.
        assertArrayEquals("Buffer should not be modified for a negative length", expectedBuffer, buffer);
    }
}