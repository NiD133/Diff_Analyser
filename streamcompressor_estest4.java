package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

import java.io.OutputStream;
import java.io.PipedOutputStream;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the StreamCompressor class.
 */
public class StreamCompressorTest {

    /**
     * Verifies that the reset() method correctly sets the byte counters for
     * bytes written and bytes read back to zero.
     */
    @Test
    public void resetShouldSetCountersToZero() {
        // Arrange: Create a StreamCompressor instance.
        // The specific type of OutputStream is not critical for this test's logic.
        OutputStream outputStream = new PipedOutputStream();
        StreamCompressor streamCompressor = StreamCompressor.create(outputStream);

        // Act: Call the method under test.
        streamCompressor.reset();

        // Assert: Verify that the relevant counters have been reset to zero.
        assertEquals("Bytes written for the last entry should be 0 after reset.",
                0L, streamCompressor.getBytesWrittenForLastEntry());
        assertEquals("Bytes read from the source should be 0 after reset.",
                0L, streamCompressor.getBytesRead());
    }
}