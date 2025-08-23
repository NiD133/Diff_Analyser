package org.apache.commons.io.input.buffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

/**
 * Unit tests for {@link CircularByteBuffer}.
 */
public class CircularByteBufferTest {

    /**
     * Tests that a new buffer created with the default constructor correctly reports
     * its initial available space.
     */
    @Test
    public void newBufferWithDefaultSizeShouldHaveFullSpace() {
        // Arrange: Create a buffer using the default constructor.
        // The default size is defined by IOUtils.DEFAULT_BUFFER_SIZE.
        final CircularByteBuffer buffer = new CircularByteBuffer();

        // Act & Assert:
        // 1. Verify the total available space is the default buffer size.
        assertEquals("A new buffer should have space equal to the default size.",
                IOUtils.DEFAULT_BUFFER_SIZE, buffer.getSpace());

        // 2. Verify it has space for a reasonable number of bytes.
        final int requestedSpace = 1024;
        assertTrue("A new buffer should report having space for " + requestedSpace + " bytes.",
                buffer.hasSpace(requestedSpace));
    }
}