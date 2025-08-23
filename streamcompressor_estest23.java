package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * This class contains tests for the StreamCompressor, focusing on specific behaviors.
 * The original test class name 'StreamCompressor_ESTestTest23' is kept for context,
 * but in a real-world scenario, it would be part of a single 'StreamCompressorTest' class.
 */
public class StreamCompressor_ESTestTest23 extends StreamCompressor_ESTest_scaffolding {

    /**
     * Verifies that flushing a newly created StreamCompressor writes the
     * DEFLATE empty block with a sync flush marker, which is 2 bytes long.
     * This ensures the internal byte counter is updated correctly after a flush operation
     * on an empty stream.
     */
    @Test(timeout = 4000)
    public void flushOnNewCompressorShouldWriteTwoBytes() throws IOException {
        // Arrange: Create a compressor that writes to an in-memory byte array.
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final StreamCompressor compressor = StreamCompressor.create(outputStream);

        // Act: Flush the deflater without writing any data to it.
        compressor.flushDeflater();
        final long bytesWritten = compressor.getBytesWrittenForLastEntry();

        // Assert: The flush operation on a new deflater writes a 2-byte sync flush marker.
        final long expectedBytes = 2L;
        assertEquals("The internal counter for bytes written should be 2 after the initial flush.",
                expectedBytes, bytesWritten);
        assertEquals("The underlying output stream should contain 2 bytes.",
                expectedBytes, outputStream.size());
    }
}