package com.itextpdf.text.pdf;

import org.junit.Test;

import java.io.EOFException;
import java.io.IOException;

import static org.junit.Assert.fail;

/**
 * This test verifies the behavior of the RandomAccessFileOrArray class,
 * specifically its handling of read operations that extend beyond the available data.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Tests that {@link RandomAccessFileOrArray#readFully(byte[], int, int)} throws an {@link EOFException}
     * when an attempt is made to read more bytes than are available in the source.
     */
    @Test
    public void readFully_whenReadingPastEndOfData_shouldThrowEOFException() throws IOException {
        // Arrange: Create a data source with 8 bytes and a reader for it.
        byte[] sourceData = new byte[8];
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(sourceData);

        // Position the reader's internal pointer 4 bytes into the data source.
        // This leaves 4 bytes remaining to be read.
        reader.readIntLE(); // Consumes 4 bytes.

        // Prepare a read request that attempts to read more bytes than are available.
        final int remainingBytes = (int) (reader.length() - reader.getFilePointer()); // 4 bytes left
        final int bytesToRead = remainingBytes + 1; // Attempt to read 5 bytes.
        byte[] destinationBuffer = new byte[bytesToRead];
        final int bufferOffset = 0;

        // Act & Assert: Verify that the readFully operation throws an EOFException.
        try {
            reader.readFully(destinationBuffer, bufferOffset, bytesToRead);
            fail("Expected an EOFException to be thrown because the read request exceeds the available data.");
        } catch (EOFException expected) {
            // This is the expected outcome. The test passes.
        }
    }
}