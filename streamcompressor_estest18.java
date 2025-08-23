package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.zip.ZipEntry;

/**
 * Contains tests for the {@link StreamCompressor} class.
 */
public class StreamCompressorTest {

    /**
     * Verifies that the deflate method throws an IOException when trying to read
     * from a PipedInputStream that is not connected to a PipedOutputStream.
     */
    @Test(expected = IOException.class)
    public void deflateShouldThrowIOExceptionWhenPipesAreNotConnected() throws IOException {
        // Arrange: Create a compressor with a PipedOutputStream. The corresponding
        // PipedInputStream is created but intentionally left unconnected.
        PipedOutputStream outputStream = new PipedOutputStream();
        StreamCompressor streamCompressor = StreamCompressor.create(outputStream);
        PipedInputStream unconnectedInputStream = new PipedInputStream();

        // Act: Attempting to deflate from the unconnected input stream. This should
        // cause an IOException when the compressor tries to read from it.
        streamCompressor.deflate(unconnectedInputStream, ZipEntry.DEFLATED);

        // Assert: The test succeeds if an IOException is thrown, as specified by the
        // 'expected' parameter in the @Test annotation.
    }
}