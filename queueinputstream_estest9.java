package org.apache.commons.io.input;

import org.junit.Test;

import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link QueueInputStream}.
 */
public class QueueInputStreamTest {

    /**
     * Tests that calling read() with an offset that is outside the bounds of the
     * destination buffer correctly throws an IndexOutOfBoundsException.
     */
    @Test
    public void testReadWithBufferThrowsExceptionForOutOfBoundsOffset() {
        // Arrange
        final QueueInputStream inputStream = new QueueInputStream();
        final byte[] emptyBuffer = new byte[0];
        final int outOfBoundsOffset = 1;
        final int anyLength = 1;

        // Act & Assert
        // The read method should throw an exception because the offset (1) is
        // greater than the buffer's length (0).
        assertThrows(IndexOutOfBoundsException.class, () -> {
            inputStream.read(emptyBuffer, outOfBoundsOffset, anyLength);
        });
    }
}