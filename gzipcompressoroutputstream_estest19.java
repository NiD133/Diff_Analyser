package org.apache.commons.compress.compressors.gzip;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.Test;

/**
 * This test class contains tests for the GzipCompressorOutputStream class.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class GzipCompressorOutputStream_ESTestTest19 extends GzipCompressorOutputStream_ESTest_scaffolding {

    /**
     * Verifies that calling the write(byte[], int, int) method with a null buffer
     * correctly throws a NullPointerException.
     */
    @Test(timeout = 4000, expected = NullPointerException.class)
    public void writeWithNullBufferShouldThrowNullPointerException() throws IOException {
        // Arrange: Set up the output stream for compression.
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GzipCompressorOutputStream gzipOutputStream = new GzipCompressorOutputStream(byteArrayOutputStream);

        // Act: Attempt to write a null buffer to the stream.
        // The @Test(expected) annotation will automatically handle the assertion
        // that a NullPointerException is thrown.
        gzipOutputStream.write(null, 10, 10);
    }
}