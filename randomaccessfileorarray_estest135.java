package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.EOFException;
import java.io.IOException;

/**
 * Test suite for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that readUnsignedInt() throws an EOFException when there are
     * not enough bytes available in the source to read a full 4-byte integer.
     */
    @Test(expected = EOFException.class)
    public void readUnsignedInt_withInsufficientBytes_throwsEOFException() throws IOException {
        // Arrange: Create a data source with only 1 byte. Reading an unsigned int requires 4 bytes.
        byte[] insufficientData = new byte[1];
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(insufficientData);

        // Act & Assert: Attempting to read an unsigned int from the truncated source
        // should throw an EOFException. The expectation is handled by the @Test annotation.
        fileOrArray.readUnsignedInt();
    }
}