package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for StreamCompressor.
 * This class contains the refactored test case.
 */
public class StreamCompressor_ESTestTest28 extends StreamCompressor_ESTest_scaffolding {

    /**
     * Verifies that calling deflate() on a StreamCompressor with no input data
     * writes the standard 2-byte end-of-stream marker for an empty DEFLATE stream.
     */
    @Test(timeout = 4000)
    public void deflateWithNoInputWritesEmptyDeflateBlock() throws IOException {
        // Arrange: Set up the StreamCompressor with a Deflater and an in-memory output stream.
        // A ByteArrayOutputStream is used to easily capture and verify the written bytes.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutput = new DataOutputStream(outputStream);
        Deflater deflater = new Deflater();
        StreamCompressor streamCompressor = StreamCompressor.create(dataOutput, deflater);

        // Act: Call deflate() without providing any input. This should flush the
        // deflater, causing it to write the end-of-stream marker.
        streamCompressor.deflate();

        // Assert: The empty DEFLATE block is exactly 2 bytes long.
        final long expectedBytesWritten = 2L;

        assertEquals("The underlying output stream should contain 2 bytes.",
                (int) expectedBytesWritten, outputStream.size());
        assertEquals("The deflater should report 2 bytes of total output.",
                expectedBytesWritten, deflater.getTotalOut());
        assertEquals("The StreamCompressor should report 2 total bytes written.",
                expectedBytesWritten, streamCompressor.getTotalBytesWritten());
    }
}