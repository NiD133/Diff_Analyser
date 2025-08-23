package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedOutputStream;

/**
 * This test class contains tests for the {@link StreamCompressor} class.
 * This particular test focuses on the behavior of the writeCounted method.
 */
public class StreamCompressor_ESTestTest9 { // Retaining original class name for context

    /**
     * Verifies that writeCounted throws an IndexOutOfBoundsException when the
     * provided offset and length are invalid for the source byte array.
     * <p>
     * The StreamCompressor is expected to delegate the write operation to the
     * underlying stream, which is responsible for performing the bounds check.
     * This test ensures that the exception from the underlying stream is propagated correctly.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void writeCountedWithInvalidBoundsShouldThrowException() throws IOException {
        // Arrange: Set up a StreamCompressor and a small source buffer.
        OutputStream underlyingStream = new PipedOutputStream();
        StreamCompressor streamCompressor = StreamCompressor.create(underlyingStream);

        byte[] sourceData = new byte[2];
        int offset = 1;
        // Define a length that is clearly out of bounds for the sourceData array.
        // (offset + lengthToWrite) > sourceData.length --> (1 + 8) > 2
        int lengthToWrite = 8;

        // Act: Attempt to write from the buffer with invalid parameters.
        // This call is expected to throw an IndexOutOfBoundsException.
        streamCompressor.writeCounted(sourceData, offset, lengthToWrite);

        // Assert: The test will pass if the expected IndexOutOfBoundsException is thrown.
        // This is handled by the @Test(expected=...) annotation.
    }
}