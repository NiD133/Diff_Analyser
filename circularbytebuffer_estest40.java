package org.apache.commons.io.input.buffer;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for {@link CircularByteBuffer}.
 */
public class CircularByteBufferTest {

    /**
     * Tests that a newly created buffer is empty and its available space
     * matches its initial capacity.
     */
    @Test
    public void newBufferShouldBeEmptyAndHaveFullSpace() {
        // Arrange: Define the buffer's capacity and create a new instance.
        final int capacity = 2;
        final CircularByteBuffer buffer = new CircularByteBuffer(capacity);

        // Assert: Verify the initial state of the new buffer.
        assertFalse("A new buffer should not contain any bytes.", buffer.hasBytes());
        assertEquals("A new buffer's space should equal its capacity.", capacity, buffer.getSpace());
    }
}