package com.itextpdf.text.pdf;

import org.junit.Test;

import java.io.EOFException;
import java.io.IOException;

/**
 * Test suite for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that calling readUTF() on a data source that is too short to contain
     * the required 2-byte length prefix results in an EOFException.
     *
     * The readUTF() method first attempts to read two bytes to determine the string length.
     * This test provides only one byte, which should trigger the exception.
     */
    @Test(expected = EOFException.class)
    public void readUTF_whenSourceIsTooShortForLengthPrefix_throwsEOFException() throws IOException {
        // Arrange: Create a data source with only one byte.
        byte[] insufficientData = new byte[1];
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(insufficientData);

        // Act & Assert: Attempting to read a UTF string should throw an EOFException.
        // The @Test(expected) annotation handles the assertion.
        fileOrArray.readUTF();
    }
}