package org.apache.commons.io.input.buffer;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the {@link CircularByteBuffer} class, focusing on its initial state.
 */
public class CircularByteBufferTest {

    /**
     * Verifies that a CircularByteBuffer initialized with zero capacity
     * is correctly reported as having no bytes and no available space.
     */
    @Test
    public void creatingBufferWithZeroCapacity_shouldHaveNoBytesAndNoSpace() {
        // Arrange: Create a buffer with zero capacity.
        final CircularByteBuffer buffer = new CircularByteBuffer(0);

        // Act: No action is performed, as we are testing the initial state after construction.

        // Assert: The buffer should be empty and simultaneously full.
        assertEquals("A zero-capacity buffer should contain 0 bytes upon creation.", 0, buffer.getCurrentNumberOfBytes());
        assertFalse("A zero-capacity buffer should have no space available.", buffer.hasSpace());
    }
}