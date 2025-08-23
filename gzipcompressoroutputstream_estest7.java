package org.apache.commons.compress.compressors.gzip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.Test;

/**
 * Unit tests for the {@link GzipCompressorOutputStream} class.
 */
public class GzipCompressorOutputStreamTest {

    /**
     * Verifies that attempting to write to a stream after it has been closed
     * results in an IOException.
     */
    @Test(timeout = 4000)
    public void writeToClosedStreamShouldThrowIOException() throws IOException {
        // Arrange: Create a GzipCompressorOutputStream and immediately close it.
        ByteArrayOutputStream underlyingStream = new ByteArrayOutputStream();
        GzipCompressorOutputStream gzipOutputStream = new GzipCompressorOutputStream(underlyingStream);
        gzipOutputStream.close();

        // Act & Assert: Attempting to write to the closed stream should throw an IOException.
        try {
            gzipOutputStream.write(new byte[0]);
            fail("Expected an IOException to be thrown when writing to a closed stream.");
        } catch (final IOException e) {
            // Verify the exception has the expected message.
            assertEquals("Stream closed", e.getMessage());
        }
    }
}