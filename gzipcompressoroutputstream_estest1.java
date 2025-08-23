package org.apache.commons.compress.compressors.gzip;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test suite for {@link GzipCompressorOutputStream}, focusing on edge cases and invalid arguments.
 */
public class GzipCompressorOutputStreamTest {

    /**
     * Verifies that calling {@code write(byte[], int, int)} with a negative length
     * throws an {@link IndexOutOfBoundsException}. This behavior is required by the
     * general contract of {@link java.io.OutputStream#write(byte[], int, int)}.
     *
     * @throws IOException if an I/O error occurs during stream setup, which is not
     *                     expected in this in-memory test.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void writeWithNegativeLengthShouldThrowIndexOutOfBoundsException() throws IOException {
        // Arrange: Set up a GzipCompressorOutputStream that writes to an in-memory buffer.
        // This avoids creating actual files and makes the test faster and more reliable.
        ByteArrayOutputStream memoryOutputStream = new ByteArrayOutputStream();
        GzipCompressorOutputStream gzipOutputStream = new GzipCompressorOutputStream(memoryOutputStream);

        byte[] buffer = new byte[64]; // A sample buffer to use for the write operation.
        int offset = 48;              // A valid offset within the buffer.
        int negativeLength = -302;    // An invalid negative length, which should trigger the exception.

        // Act: Attempt to write data using the invalid negative length.
        gzipOutputStream.write(buffer, offset, negativeLength);

        // Assert: The test framework will automatically pass the test if the expected
        // IndexOutOfBoundsException is thrown. If no exception is thrown, the test will fail.
    }
}