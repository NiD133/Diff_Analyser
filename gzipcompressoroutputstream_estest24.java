package org.apache.commons.compress.compressors.gzip;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Tests for {@link GzipCompressorOutputStream}.
 */
public class GzipCompressorOutputStreamTest {

    /**
     * Verifies that calling close() on a GzipCompressorOutputStream multiple times
     * is safe and does not throw an exception. This behavior is part of the
     * general contract for java.io.Closeable.
     */
    @Test
    public void closeCanBeCalledMultipleTimesWithoutError() throws IOException {
        // Arrange: Create a GzipCompressorOutputStream wrapping an in-memory stream.
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GzipCompressorOutputStream gzipOutputStream = new GzipCompressorOutputStream(byteArrayOutputStream);

        // Act: Close the stream for the first time. This is the primary action.
        gzipOutputStream.close();

        // Assert: Closing the stream again should not throw an exception.
        // The test implicitly passes if the following line executes without error.
        gzipOutputStream.close();
    }
}