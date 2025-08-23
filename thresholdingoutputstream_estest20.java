package org.apache.commons.io.output;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link ThresholdingOutputStream} class.
 */
public class ThresholdingOutputStreamTest {

    /**
     * Tests that the setByteCount() method correctly updates the internal byte counter,
     * which can be useful for re-opening a stream.
     */
    @Test
    public void testSetByteCount() {
        // Arrange: Create a stream with an arbitrary threshold.
        final int arbitraryThreshold = 100;
        final ThresholdingOutputStream stream = new ThresholdingOutputStream(arbitraryThreshold);
        final long newByteCount = 1L;

        // Act: Set the byte count directly.
        stream.setByteCount(newByteCount);

        // Assert: Verify that the byte count was updated.
        assertEquals("The byte count should be updated to the new value.", newByteCount, stream.getByteCount());
    }
}