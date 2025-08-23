package org.apache.commons.compress.compressors.gzip;

import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import org.junit.Test;

/**
 * Contains understandable tests for the {@link GzipCompressorOutputStream} class.
 */
public class GzipCompressorOutputStreamTest {

    /**
     * Verifies that a GzipCompressorOutputStream can be instantiated and subsequently
     * wrapped by another OutputStream, like ObjectOutputStream, without errors.
     * This test confirms the constructor's basic functionality and its compatibility
     * as a standard Java OutputStream.
     */
    @Test
    public void shouldAllowWrappingInAnotherOutputStream() throws IOException {
        // Arrange: Set up an in-memory byte stream to capture the output. This avoids
        // writing to the actual file system, making the test faster and more reliable.
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // Act & Assert: Create the Gzip stream and wrap it. The test passes if no
        // exceptions are thrown during construction and the final stream is not null.
        // Using try-with-resources ensures proper stream closure.
        try (GzipCompressorOutputStream gzipOutputStream = new GzipCompressorOutputStream(byteArrayOutputStream);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(gzipOutputStream)) {

            assertNotNull("The wrapped ObjectOutputStream should be successfully created.", objectOutputStream);
        }
    }
}