package org.apache.commons.io.input.buffer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for the {@link CircularByteBuffer} class.
 */
public class CircularByteBufferTest {

    /**
     * Tests the behavior of the hasSpace() methods on a buffer with zero capacity.
     * A buffer with zero capacity should correctly report that it has space for zero bytes,
     * but no space for one or more bytes.
     */
    @Test
    public void testHasSpaceWithZeroCapacityBuffer() {
        // Arrange: Create a buffer with zero capacity, which is a valid edge case.
        final CircularByteBuffer zeroCapacityBuffer = new CircularByteBuffer(0);

        // Act & Assert

        // A buffer should always have space for 0 bytes, even if its capacity is 0.
        assertTrue("A zero-capacity buffer should have space for 0 bytes.",
                zeroCapacityBuffer.hasSpace(0));

        // The hasSpace() method without arguments checks for space for at least one byte.
        // A zero-capacity buffer has no space for any bytes.
        assertFalse("A zero-capacity buffer should not have space for one or more bytes.",
                zeroCapacityBuffer.hasSpace());
    }
}