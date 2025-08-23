package org.apache.commons.compress.compressors.gzip;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Unit tests for the GzipCompressorOutputStream class.
 */
public class GzipCompressorOutputStreamTest {

    /**
     * Verifies that the close() method correctly marks the stream as closed.
     */
    @Test
    public void closeMethodShouldMarkStreamAsClosed() throws IOException {
        // Arrange: Set up a GzipCompressorOutputStream.
        // A ByteArrayOutputStream is a standard and simple in-memory sink for the compressed data.
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GzipCompressorOutputStream gzipOutputStream = new GzipCompressorOutputStream(byteArrayOutputStream);

        // Assert that the stream is initially open.
        assertFalse("Stream should be open upon creation.", gzipOutputStream.isClosed());

        // Act: Close the stream.
        gzipOutputStream.close();

        // Assert: Verify the stream reports itself as closed.
        assertTrue("Stream should be marked as closed after calling close().", gzipOutputStream.isClosed());
    }
}