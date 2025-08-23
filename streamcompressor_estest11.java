package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedOutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link StreamCompressor}.
 */
public class StreamCompressorTest {

    /**
     * Tests that StreamCompressor correctly propagates an IOException thrown by the
     * underlying output stream when writeCounted is called.
     */
    @Test
    public void writeCountedShouldPropagateIOExceptionFromUnderlyingStream() {
        // Arrange: Create a StreamCompressor with an underlying stream that is known to fail.
        // A PipedOutputStream will throw an IOException if written to before being connected
        // to a PipedInputStream. This simulates a failing stream.
        PipedOutputStream unconnectedPipe = new PipedOutputStream();
        StreamCompressor compressor = StreamCompressor.create((OutputStream) unconnectedPipe);
        byte[] dummyData = new byte[1024];

        // Act & Assert: Verify that the IOException from the underlying stream is propagated.
        try {
            compressor.writeCounted(dummyData, 0, dummyData.length);
            fail("Expected an IOException to be thrown due to the unconnected pipe.");
        } catch (final IOException e) {
            // Assert: Check that the caught exception is the one expected from the PipedOutputStream.
            assertEquals("Pipe not connected", e.getMessage());
        }
    }
}