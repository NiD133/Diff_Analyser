package org.apache.commons.compress.compressors.gzip;

import org.junit.Test;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Tests for {@link GzipCompressorOutputStream}.
 */
public class GzipCompressorOutputStreamTest {

    /**
     * Verifies that writing a byte to the stream does not automatically close it.
     */
    @Test
    public void writeShouldNotImplicitlyCloseStream() throws IOException {
        // Arrange: Set up an in-memory output stream to capture the compressed data
        // and wrap it with the GzipCompressorOutputStream.
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GzipCompressorOutputStream gzipOutputStream = new GzipCompressorOutputStream(byteArrayOutputStream);

        try {
            // Act: Write a single byte to the stream.
            gzipOutputStream.write(100);

            // Assert: The stream should remain open. We can confirm this by attempting
            // a second write. If the stream were closed, this would throw an IOException.
            gzipOutputStream.write(101);
            // Reaching this line means the stream was still open, so the test passes.

        } catch (IOException e) {
            fail("The stream should have remained open after a write operation, but it appears to be closed: " + e.getMessage());
        } finally {
            // Cleanup: Always ensure the stream is closed after the test.
            gzipOutputStream.close();
        }
    }
}