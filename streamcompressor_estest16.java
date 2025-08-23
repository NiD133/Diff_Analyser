package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedOutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link StreamCompressor} class.
 */
public class StreamCompressorTest {

    /**
     * Verifies that flushDeflater() correctly propagates an IOException when the
     * underlying output stream is not ready for writing.
     */
    @Test
    public void flushDeflaterShouldThrowIOExceptionWhenPipeIsNotConnected() {
        // Arrange: Create a StreamCompressor with an unconnected PipedOutputStream.
        // This simulates a stream that is not ready for writing.
        PipedOutputStream unconnectedPipe = new PipedOutputStream();
        StreamCompressor compressor = StreamCompressor.create((OutputStream) unconnectedPipe);

        // Act & Assert: Attempting to flush should throw an IOException.
        try {
            compressor.flushDeflater();
            fail("Expected an IOException because the underlying pipe is not connected.");
        } catch (IOException e) {
            // Verify that the expected exception was thrown with the correct message,
            // confirming the failure is due to the unconnected pipe.
            assertEquals("Pipe not connected", e.getMessage());
        }
    }
}