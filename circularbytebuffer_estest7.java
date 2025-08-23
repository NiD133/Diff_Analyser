package org.apache.commons.io.input.buffer;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

// Unnecessary imports from the original test have been removed.

public class CircularByteBuffer_ESTestTest7 extends CircularByteBuffer_ESTest_scaffolding {

    /**
     * Tests that peek() returns false when the buffer contains fewer bytes
     * than the requested peek length.
     */
    @Test(timeout = 4000)
    public void peekShouldReturnFalseWhenBufferHasInsufficientBytes() {
        // Arrange: Create an empty buffer and data to compare against.
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] dataToCompare = { 42 }; // Use an explicit, non-zero value.

        // Ensure the buffer is empty by adding and then removing a byte.
        buffer.add((byte) 99);
        buffer.read();
        assertFalse("Precondition failed: Buffer should be empty before peeking.", buffer.hasBytes());

        // Act: Attempt to peek for one byte from the now-empty buffer.
        final int offset = 0;
        final int length = 1;
        final boolean peekResult = buffer.peek(dataToCompare, offset, length);

        // Assert: Verify that peek returned false and did not alter the buffer's state.
        assertFalse("peek() should return false as the buffer has fewer bytes (0) than requested (1).", peekResult);
        assertFalse("Buffer should remain empty after a failed peek operation.", buffer.hasBytes());
    }
}