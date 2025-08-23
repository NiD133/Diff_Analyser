package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

import java.io.IOException;
import java.io.PipedOutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test for the {@link StreamCompressor} class.
 */
public class StreamCompressorTest {

    /**
     * Tests that {@link StreamCompressor#writeCounted(byte[])} correctly propagates an
     * {@link IOException} when the underlying {@link java.io.OutputStream} fails.
     *
     * <p>This is verified by attempting to write to an unconnected {@link PipedOutputStream},
     * which is guaranteed to throw an IOException.</p>
     */
    @Test
    public void writeCountedShouldPropagateIOExceptionFromUnderlyingStream() {
        // Arrange: Create a StreamCompressor with an output stream that is known to fail on write.
        // A PipedOutputStream that is not connected to a PipedInputStream will throw
        // an IOException with the message "Pipe not connected".
        PipedOutputStream unconnectedPipe = new PipedOutputStream();
        StreamCompressor streamCompressor = StreamCompressor.create(unconnectedPipe);
        byte[] dummyData = new byte[1];

        // Act & Assert: Verify that the expected IOException is thrown and its message is correct.
        try {
            streamCompressor.writeCounted(dummyData);
            fail("Expected an IOException to be thrown because the underlying pipe is not connected.");
        } catch (final IOException e) {
            // The StreamCompressor should faithfully propagate the exception from the PipedOutputStream.
            assertEquals("Pipe not connected", e.getMessage());
        }
    }
}