package org.apache.commons.compress.compressors.gzip;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Contains tests for the GzipCompressorOutputStream class.
 * This refactored test focuses on verifying the state of the stream after construction.
 */
// The original class name and inheritance are preserved to fit into the existing test suite structure.
public class GzipCompressorOutputStream_ESTestTest21 extends GzipCompressorOutputStream_ESTest_scaffolding {

    /**
     * Verifies that a GzipCompressorOutputStream is open and writable
     * immediately after its construction with custom parameters.
     */
    @Test(timeout = 4000)
    public void streamShouldBeWritableImmediatelyAfterCreation() throws IOException {
        // Arrange: Set up GZIP parameters and a simple in-memory output stream.
        GzipParameters gzipParameters = new GzipParameters();
        gzipParameters.setCompressionLevel(9); // Use a non-default compression level for a more robust test.

        ByteArrayOutputStream underlyingOutputStream = new ByteArrayOutputStream();

        // Act: Create the GzipCompressorOutputStream instance.
        GzipCompressorOutputStream gzipOutputStream = new GzipCompressorOutputStream(underlyingOutputStream, gzipParameters);

        // Assert: The stream should be open and accept data. A write operation
        // would throw an IOException if the stream were closed. This test passes
        // if no exception is thrown, implicitly confirming the stream is open.
        gzipOutputStream.write(42); // Write a single byte to confirm writability.
    }
}