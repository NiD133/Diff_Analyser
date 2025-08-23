package com.itextpdf.text.pdf;

import org.junit.Test;

import java.io.EOFException;
import java.io.IOException;

/**
 * Unit tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that readUnsignedShortLE() throws an EOFException when there are
     * not enough bytes remaining in the source to read a short (which requires 2 bytes).
     */
    @Test(expected = EOFException.class)
    public void readUnsignedShortLE_shouldThrowEOFException_whenSourceHasInsufficientBytes() throws IOException {
        // Arrange: Create a data source with only 1 byte, which is less than the 2 bytes
        // required to read a short integer.
        byte[] insufficientData = new byte[1];
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(insufficientData);

        // Act: Attempt to read an unsigned short from the undersized data source.
        fileOrArray.readUnsignedShortLE();

        // Assert: An EOFException is expected, as declared by the @Test annotation.
    }
}