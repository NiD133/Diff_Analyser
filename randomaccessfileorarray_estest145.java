package com.itextpdf.text.pdf;

import org.junit.Test;

import java.io.EOFException;
import java.io.IOException;

/**
 * Test suite for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that calling readShort() on a data source with insufficient bytes
     * (i.e., less than the 2 bytes required for a short) correctly throws an EOFException.
     */
    @Test(expected = EOFException.class)
    public void readShort_whenNotEnoughBytesAvailable_throwsEOFException() throws IOException {
        // Arrange: Create a data source with only one byte.
        byte[] insufficientData = new byte[1];
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(insufficientData);

        // Act: Attempt to read a short, which requires two bytes.
        fileOrArray.readShort();

        // Assert: An EOFException is expected, as declared in the @Test annotation.
    }
}