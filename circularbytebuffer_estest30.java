package org.apache.commons.io.input.buffer;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Test suite for {@link CircularByteBuffer}.
 * This class contains the refactored test case.
 */
public class CircularByteBufferTest {

    /**
     * Tests that peek() for a zero-length sequence returns false and does not
     * consume any bytes from the buffer.
     */
    @Test
    public void peekWithZeroLengthShouldReturnFalseAndNotConsumeBytes() {
        // Arrange: Create a buffer and add one byte to it to ensure it's not empty.
        final CircularByteBuffer buffer = new CircularByteBuffer();
        buffer.add((byte) 'a');
        
        // The content of this array is irrelevant for a zero-length peek.
        final byte[] comparisonBuffer = new byte[1];

        // Act: Peek for a zero-length sequence.
        final boolean result = buffer.peek(comparisonBuffer, 0, 0);

        // Assert: Verify that peek() returns false and the buffer state is unchanged.
        assertFalse("Peeking for a zero-length sequence should return false.", result);
        assertEquals("Buffer size should remain unchanged after a peek operation.",
                     1, buffer.getCurrentNumberOfBytes());
    }
}