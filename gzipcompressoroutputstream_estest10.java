package org.apache.commons.compress.compressors.gzip;

import java.io.IOException;
import org.evosuite.runtime.mock.java.io.MockFileOutputStream;
import org.junit.Test;

/**
 * Unit tests for the GzipCompressorOutputStream class.
 */
public class GzipCompressorOutputStreamTest {

    /**
     * Verifies that calling finish() throws an IOException if the underlying
     * stream has already been closed. This is expected behavior, as the
     * finish() method needs to write trailer data to the stream.
     */
    @Test(expected = IOException.class)
    public void finishShouldThrowIOExceptionWhenUnderlyingStreamIsClosed() throws IOException {
        // Arrange: Create a GZIP output stream and then close its underlying stream prematurely.
        MockFileOutputStream underlyingStream = new MockFileOutputStream("test.gz");
        GzipCompressorOutputStream gzipOutputStream = new GzipCompressorOutputStream(underlyingStream);

        underlyingStream.close();

        // Act & Assert: Attempting to finish the GZIP stream should now fail.
        // The @Test(expected) annotation asserts that an IOException is thrown.
        gzipOutputStream.finish();
    }
}