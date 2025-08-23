package org.apache.commons.compress.compressors.gzip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.PipedOutputStream;
import org.junit.Test;

/**
 * Tests for {@link GzipCompressorOutputStream}.
 */
public class GzipCompressorOutputStreamTest {

    /**
     * Tests that the GzipCompressorOutputStream constructor throws an IOException
     * if it's given a PipedOutputStream that is not connected to a PipedInputStream.
     * This is expected because the constructor immediately attempts to write the GZIP
     * header to the output stream, which fails on an unconnected pipe.
     */
    @Test
    public void constructorShouldThrowIOExceptionForUnconnectedPipe() {
        // Arrange: Create an unconnected piped output stream.
        final PipedOutputStream unconnectedPipe = new PipedOutputStream();
        final GzipParameters parameters = new GzipParameters();

        // Act & Assert: Attempting to create the GzipCompressorOutputStream should fail.
        try {
            new GzipCompressorOutputStream(unconnectedPipe, parameters);
            fail("Expected an IOException because the pipe is not connected.");
        } catch (final IOException e) {
            // Assert: Verify that the exception is the one expected from PipedOutputStream.
            assertEquals("Pipe not connected", e.getMessage());
        }
    }
}