package com.itextpdf.text.pdf;

import org.junit.Test;

import java.io.EOFException;
import java.io.IOException;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that readFloat() throws an EOFException if fewer than 4 bytes are available,
     * even when some of the available bytes were pushed back.
     */
    @Test(expected = EOFException.class)
    public void readFloat_whenFewerThanFourBytesAvailableWithPushBack_throwsEOFException() throws IOException {
        // Arrange: A float requires 4 bytes. We create a source with only 3 bytes available
        // for reading (2 bytes in the initial array + 1 byte pushed back).
        byte[] insufficientBytesForFloat = new byte[2];
        RandomAccessFileOrArray randomAccess = new RandomAccessFileOrArray(insufficientBytesForFloat);
        randomAccess.pushBack((byte) 0);

        // Act & Assert: Attempting to read a float should throw an EOFException because
        // there is not enough data. The assertion is handled by the @Test(expected=...) annotation.
        randomAccess.readFloat();
    }
}