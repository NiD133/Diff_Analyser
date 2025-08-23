package org.apache.commons.compress.compressors.gzip;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class GzipCompressorOutputStream_ESTestTest5 extends GzipCompressorOutputStream_ESTest_scaffolding {

    @Test
    public void writeAfterCloseThrowsIOException() throws IOException {
        // Arrange: Create a GzipCompressorOutputStream and then close it.
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GzipCompressorOutputStream gzipOutputStream = new GzipCompressorOutputStream(byteArrayOutputStream);
        gzipOutputStream.close();

        // Act & Assert: Attempting to write to the closed stream should fail.
        try {
            gzipOutputStream.write(new byte[1], 0, 1);
            fail("Expected an IOException to be thrown when writing to a closed stream.");
        } catch (IOException e) {
            // Verify that the exception has the expected message.
            assertEquals("Stream closed", e.getMessage());
        }
    }
}