package com.itextpdf.text.pdf;

import org.junit.Test;

import java.io.EOFException;
import java.io.IOException;

/**
 * Test suite for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that attempting to read a boolean from an empty data source
     * correctly throws an EOFException.
     */
    @Test(expected = EOFException.class)
    public void readBoolean_fromEmptySource_shouldThrowEOFException() throws IOException {
        // Arrange: Create a RandomAccessFileOrArray instance with an empty byte array.
        byte[] emptySource = new byte[0];
        RandomAccessFileOrArray randomAccessFileOrArray = new RandomAccessFileOrArray(emptySource);

        // Act & Assert: Attempting to read from the empty source should throw an EOFException.
        randomAccessFileOrArray.readBoolean();
    }
}