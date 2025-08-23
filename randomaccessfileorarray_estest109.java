package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.EOFException;
import java.io.IOException;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class, focusing on its data reading capabilities.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that attempting to read a 4-byte integer from a source with insufficient
     * data correctly throws an EOFException.
     *
     * The readIntLE() method requires 4 bytes to be available. This test provides a source
     * with only 1 byte to ensure that boundary conditions are handled properly.
     */
    @Test(expected = EOFException.class)
    public void readIntLEWithInsufficientBytesThrowsEOFException() throws IOException {
        // Arrange: Create a data source with only one byte, which is not enough to read an integer.
        byte[] insufficientData = new byte[1];
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(insufficientData);

        // Act & Assert: Attempt to read a little-endian integer.
        // An EOFException is expected, as specified by the @Test annotation.
        fileOrArray.readIntLE();
    }
}