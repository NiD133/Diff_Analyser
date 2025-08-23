package org.apache.commons.io.input.buffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

/**
 * Unit tests for {@link CircularByteBuffer}.
 */
public class CircularByteBufferTest {

    /**
     * Tests that hasSpace() returns false when the requested space is larger
     * than the buffer's total capacity, and that the check does not alter the
     * buffer's state.
     */
    @Test
    public void hasSpaceShouldReturnFalseWhenRequestedSpaceExceedsCapacity() {
        // Arrange: Create a buffer with the default capacity.
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final int defaultCapacity = IOUtils.DEFAULT_BUFFER_SIZE;

        // Act: Check if the buffer has space for a value far exceeding its capacity.
        final boolean hasSpace = buffer.hasSpace(Integer.MAX_VALUE);

        // Assert: The check should return false, and the available space should be unchanged.
        assertFalse("hasSpace() should return false for a request larger than the capacity.", hasSpace);
        assertEquals("Checking for space should not alter the buffer's state.",
                     defaultCapacity, buffer.getSpace());
    }
}