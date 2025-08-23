package org.apache.commons.io.input.buffer;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link CircularByteBuffer}.
 */
public class CircularByteBufferTest {

    /**
     * Tests that a newly created buffer using the default constructor
     * is empty and reports having the full default capacity available.
     */
    @Test
    public void newDefaultBufferShouldHaveFullSpace() {
        // Arrange: Create a new buffer with the default size.
        final CircularByteBuffer buffer = new CircularByteBuffer();

        // Act & Assert: Verify the initial state of the buffer.
        // A new buffer should report that it has space available.
        assertTrue("A new buffer should have space", buffer.hasSpace());

        // The available space should be equal to the default buffer size.
        assertEquals("A new buffer's space should equal the default buffer size",
                IOUtils.DEFAULT_BUFFER_SIZE, buffer.getSpace());
    }
}