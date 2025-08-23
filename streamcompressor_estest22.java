package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedOutputStream;
import java.util.zip.Deflater;

/**
 * Contains tests for the {@link StreamCompressor} class.
 * This refactored test focuses on a specific failure scenario.
 */
// The original test extended a scaffolding class, which is preserved here.
public class StreamCompressor_ESTestTest22 extends StreamCompressor_ESTest_scaffolding {

    /**
     * Verifies that calling deflate() on a StreamCompressor created with a null Deflater
     * results in a NullPointerException.
     */
    @Test(expected = NullPointerException.class, timeout = 4000)
    public void deflateWithNullDeflaterShouldThrowNullPointerException() throws IOException {
        // Arrange: Create a StreamCompressor with a null Deflater.
        // A PipedOutputStream is used as a dummy output; it won't be written to
        // because the operation is expected to fail beforehand.
        OutputStream dummyOutputStream = new PipedOutputStream();
        StreamCompressor streamCompressor = StreamCompressor.create(dummyOutputStream, (Deflater) null);

        // Act & Assert: Calling deflate() should immediately throw a NullPointerException,
        // which is verified by the `expected` attribute of the @Test annotation.
        streamCompressor.deflate();
    }
}