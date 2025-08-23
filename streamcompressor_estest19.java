package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PipedOutputStream;
import java.util.zip.Deflater;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test class contains tests for the StreamCompressor class.
 * Note: The original class name 'StreamCompressor_ESTestTest19' was preserved
 * for context, but would ideally be renamed to 'StreamCompressorTest'.
 */
public class StreamCompressor_ESTestTest19 {

    /**
     * Tests that calling deflate() on a StreamCompressor configured with an
     * unconnected PipedOutputStream correctly throws an IOException.
     */
    @Test(timeout = 4000)
    public void deflateShouldThrowIOExceptionWhenWritingToUnconnectedPipe() {
        // Arrange: Create a StreamCompressor that writes to an unconnected pipe.
        // A PipedOutputStream must be connected to a PipedInputStream before it can be used.
        PipedOutputStream unconnectedPipe = new PipedOutputStream();
        DataOutputStream dataOut = new DataOutputStream(unconnectedPipe);
        Deflater deflater = new Deflater();
        StreamCompressor streamCompressor = StreamCompressor.create(dataOut, deflater);

        // Act & Assert: Expect an IOException with a specific message when trying to write.
        try {
            streamCompressor.deflate();
            fail("Expected an IOException because the underlying pipe is not connected.");
        } catch (IOException e) {
            // The underlying PipedOutputStream is expected to throw this specific exception.
            assertEquals("Pipe not connected", e.getMessage());
        }
    }
}