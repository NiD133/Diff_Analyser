package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedOutputStream;
import java.util.zip.Deflater;

/**
 * Contains tests for the {@link StreamCompressor} class, focusing on edge cases for the write method.
 */
public class StreamCompressorTest {

    /**
     * Verifies that the write() method throws an ArrayIndexOutOfBoundsException
     * when called with a negative offset and a negative length.
     * <p>
     * This behavior is expected because the underlying CRC32 checksum calculation
     * does not permit negative array indices or lengths.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void writeWithNegativeOffsetAndLengthShouldThrowException() throws IOException {
        // Arrange: Set up the StreamCompressor and define invalid input parameters.
        // A simple OutputStream is sufficient as the exception occurs before any data is written.
        OutputStream outputStream = new PipedOutputStream();
        StreamCompressor streamCompressor = StreamCompressor.create(outputStream);

        byte[] data = new byte[10]; // The array content is irrelevant.
        int invalidOffset = -1;
        int invalidLength = -1;
        // The compression method is also irrelevant for this specific exception but is a required argument.
        int compressionMethod = Deflater.DEFLATED;

        // Act: Attempt to write with invalid parameters.
        // The @Test(expected=...) annotation will automatically handle the assertion,
        // failing the test if the expected exception is not thrown.
        streamCompressor.write(data, invalidOffset, invalidLength, compressionMethod);
    }
}