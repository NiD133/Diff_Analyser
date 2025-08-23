package org.apache.commons.io.input.buffer;

import static org.junit.Assert.assertEquals;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

/**
 * Tests for {@link CircularByteBuffer}.
 */
public class CircularByteBufferTest {

    /**
     * Tests that calling the add() method with a length of zero does not
     * add any data or change the buffer's available space.
     */
    @Test
    public void addWithZeroLengthShouldNotChangeAvailableSpace() {
        // Arrange: Create a buffer with the default capacity.
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final int initialSpace = buffer.getSpace();

        // Verify our assumption about the initial state.
        assertEquals("A new buffer should have space equal to the default buffer size",
                IOUtils.DEFAULT_BUFFER_SIZE, initialSpace);

        final byte[] sourceData = new byte[10];

        // Act: Attempt to add zero bytes from the source array.
        buffer.add(sourceData, 0, 0);

        // Assert: The available space should remain unchanged.
        assertEquals("Adding zero bytes should not alter the buffer's available space",
                initialSpace, buffer.getSpace());
    }
}