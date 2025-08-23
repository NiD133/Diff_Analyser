package org.apache.commons.compress.compressors.gzip;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Contains tests for the {@link GzipCompressorOutputStream} class.
 */
public class GzipCompressorOutputStreamTest {

    /**
     * Verifies that calling finish() on a stream that has already been closed
     * throws a NullPointerException. This is the expected behavior because the
     * underlying Deflater object is set to null upon closing the stream.
     */
    @Test(expected = NullPointerException.class)
    public void callingFinishOnClosedStreamThrowsNullPointerException() throws IOException {
        // Arrange: Create a GzipCompressorOutputStream and close it.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GzipCompressorOutputStream gzipOut = new GzipCompressorOutputStream(baos);
        gzipOut.close();

        // Act: Attempt to finish the already-closed stream.
        // This call is expected to throw the NullPointerException.
        gzipOut.finish();
    }
}