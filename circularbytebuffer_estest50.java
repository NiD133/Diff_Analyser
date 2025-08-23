package org.apache.commons.io.input.buffer;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link CircularByteBuffer}.
 */
public class CircularByteBufferTest {

    @Test
    public void clear_onNewEmptyBuffer_resetsSpaceToFullCapacity() {
        // Arrange
        // A new buffer is created with the default size, so it's initially empty.
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final int expectedCapacity = IOUtils.DEFAULT_BUFFER_SIZE;

        // Act
        // The clear operation is called on the already empty buffer.
        buffer.clear();

        // Assert
        // The available space should be equal to the buffer's total capacity.
        assertEquals("After clearing a new buffer, space should equal its full capacity.",
                expectedCapacity, buffer.getSpace());
    }
}