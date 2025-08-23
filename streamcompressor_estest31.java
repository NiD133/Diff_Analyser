package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.OutputStream;
import java.io.PipedOutputStream;

/**
 * Unit tests for the {@link StreamCompressor} class.
 */
// The original test extended a scaffolding class, likely for test generation purposes.
// It is retained here to reflect the original structure, though it might not be
// necessary for a manually written test.
public class StreamCompressorTest extends StreamCompressor_ESTest_scaffolding {

    /**
     * Verifies that a newly created StreamCompressor reports zero bytes
     * written for the last entry before any data has been processed.
     */
    @Test
    public void getBytesWrittenForLastEntryShouldReturnZeroInitially() {
        // Arrange
        // A PipedOutputStream is used as a simple sink for the compressor.
        // No data will actually be written in this test.
        OutputStream outputStream = new PipedOutputStream();
        StreamCompressor compressor = StreamCompressor.create(outputStream);

        // Act
        long bytesWritten = compressor.getBytesWrittenForLastEntry();

        // Assert
        assertEquals("A new StreamCompressor should have 0 bytes written for the last entry.", 0L, bytesWritten);
    }
}