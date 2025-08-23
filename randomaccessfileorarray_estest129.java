package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.EOFException;
import java.io.IOException;

/**
 * This class contains tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArray_ESTestTest129 { // Note: Class name kept for consistency with original file.

    /**
     * Verifies that attempting to read a long (8 bytes) from a data source
     * that is too small results in an EOFException.
     */
    @Test(expected = EOFException.class)
    public void readLong_whenSourceHasInsufficientBytes_throwsEOFException() throws IOException {
        // Arrange: Create a byte array that is smaller than the size of a long.
        // A long requires 8 bytes, so we use a 7-byte array to trigger the error.
        byte[] insufficientData = new byte[Long.BYTES - 1];
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(insufficientData);

        // Act: Attempt to read a long from the undersized data source.
        fileOrArray.readLong();

        // Assert: An EOFException is expected, as specified by the @Test annotation.
    }
}