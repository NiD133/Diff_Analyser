package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link StreamCompressor} class.
 * Note: The original test class name and inheritance from a scaffolding class
 * were removed for clarity and to create a minimal, self-contained example.
 */
public class StreamCompressorTest {

    /**
     * Verifies that calling the write() method with a length of zero
     * results in no data being written and all internal counters remaining unchanged.
     */
    @Test
    public void writeWithZeroLengthShouldHaveNoEffect() throws IOException {
        // Arrange
        // Use a ByteArrayOutputStream as a simple in-memory sink for the compressed data.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        StreamCompressor streamCompressor = StreamCompressor.create(outputStream);
        byte[] buffer = new byte[10]; // The buffer content and size are irrelevant for this test.

        // Act
        // Attempt to write zero bytes from the buffer.
        long bytesWritten = streamCompressor.write(buffer, 0, 0, ZipEntry.DEFLATED);

        // Assert
        // 1. The method should report that zero bytes were written.
        assertEquals("Return value for a zero-length write should be 0", 0L, bytesWritten);

        // 2. Internal counters should remain at their initial state.
        assertEquals("Total bytes written counter should be 0", 0L, streamCompressor.getTotalBytesWritten());
        assertEquals("Bytes read counter should be 0", 0L, streamCompressor.getBytesRead());

        // 3. The underlying output stream should be empty.
        assertEquals("Underlying output stream should not be written to", 0, outputStream.size());
    }
}