package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.parallel.ScatterGatherBackingStore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link StreamCompressor} class.
 */
public class StreamCompressorTest {

    /**
     * Verifies that a newly created StreamCompressor instance initializes its
     * 'total bytes written' counter to zero. This confirms the correct
     * initial state of the object upon instantiation.
     */
    @Test
    public void newlyCreatedCompressorShouldHaveZeroTotalBytesWritten() {
        // Arrange
        // The factory method is called with a null ScatterGatherBackingStore because
        // this test only verifies the initial state of the compressor, not its
        // interaction with a backing store.

        // Act
        StreamCompressor compressor = StreamCompressor.create((ScatterGatherBackingStore) null);

        // Assert
        assertEquals("A new StreamCompressor should have a totalBytesWritten count of 0.",
                0L, compressor.getTotalBytesWritten());
    }
}