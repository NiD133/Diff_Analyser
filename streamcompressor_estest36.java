package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

import java.io.IOException;
import java.io.PipedOutputStream;
import java.util.zip.ZipEntry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link StreamCompressor}.
 */
public class StreamCompressorTest {

    /**
     * Verifies that attempting to write to a StreamCompressor backed by an
     * unconnected PipedOutputStream correctly propagates the resulting IOException.
     */
    @Test
    public void writeToUnconnectedPipeShouldThrowIOException() {
        // Arrange: Create a StreamCompressor with a PipedOutputStream that is not
        // connected to a PipedInputStream.
        PipedOutputStream unconnectedPipe = new PipedOutputStream();
        StreamCompressor compressor = StreamCompressor.create(unconnectedPipe);
        byte[] dataToWrite = new byte[16];

        // Act & Assert: The write operation should fail because the underlying pipe is not connected.
        try {
            compressor.write(dataToWrite, 0, dataToWrite.length, ZipEntry.STORED);
            fail("Expected an IOException to be thrown due to the unconnected pipe.");
        } catch (final IOException e) {
            // Verify that the exception is the one expected from PipedOutputStream.
            assertEquals("Pipe not connected", e.getMessage());
        }
    }
}