package org.apache.commons.io.input.buffer;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Contains unit tests for the {@link CircularByteBuffer} class.
 */
public class CircularByteBufferTest {

    /**
     * Tests that the peek() method returns false when the buffer contains fewer
     * bytes than the number requested to be peeked.
     */
    @Test
    public void peekShouldReturnFalseWhenNotEnoughBytesAreAvailable() {
        // Arrange: Create a buffer with a capacity of 2 and add a single byte.
        final int bufferCapacity = 2;
        final CircularByteBuffer buffer = new CircularByteBuffer(bufferCapacity);
        buffer.add((byte) 105); // The buffer now contains 1 byte.

        // The target array for peek; its contents are irrelevant for this test.
        final byte[] targetArray = new byte[4];
        final int offset = 0;
        final int lengthToPeek = 2; // Attempt to peek 2 bytes, but only 1 is available.

        // Act: Call the peek method.
        final boolean peekResult = buffer.peek(targetArray, offset, lengthToPeek);

        // Assert: Verify that the peek operation failed and the buffer's state is unchanged.
        assertFalse("peek() should return false as the requested length (2) exceeds available bytes (1).", peekResult);
        assertEquals("The available space should remain 1 after a failed peek.", 1, buffer.getSpace());
    }
}