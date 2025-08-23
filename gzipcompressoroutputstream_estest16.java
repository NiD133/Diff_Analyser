package org.apache.commons.compress.compressors.gzip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.PipedOutputStream;
import org.junit.Test;

/**
 * Contains tests for the {@link GzipCompressorOutputStream} class, focusing on
 * its constructor behavior with problematic underlying streams.
 */
// The original test extended a scaffolding class. We retain it to show a minimal change.
public class GzipCompressorOutputStreamTest extends GzipCompressorOutputStream_ESTest_scaffolding {

    /**
     * Tests that the GzipCompressorOutputStream constructor throws an IOException
     * when initialized with an unconnected PipedOutputStream.
     * <p>
     * The constructor immediately attempts to write a GZIP header to the underlying
     * stream. This action will fail on an unconnected pipe, which is the expected
     * behavior being verified.
     */
    @Test
    public void constructorShouldThrowIOExceptionForUnconnectedPipe() {
        // Arrange: Create a PipedOutputStream that is not connected to a PipedInputStream.
        PipedOutputStream unconnectedPipe = new PipedOutputStream();

        // Act & Assert: Attempting to create the GzipCompressorOutputStream should fail.
        try {
            new GzipCompressorOutputStream(unconnectedPipe);
            fail("Expected an IOException because the underlying pipe is not connected.");
        } catch (final IOException e) {
            // Verify that the exception is the one expected from an unconnected PipedOutputStream.
            assertEquals("Pipe not connected", e.getMessage());
        }
    }
}