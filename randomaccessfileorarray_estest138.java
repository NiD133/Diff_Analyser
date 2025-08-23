package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.EOFException;
import java.io.IOException;

/**
 * Test suite for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that attempting to read a double from an empty byte array source
     * results in an EOFException. A double requires 8 bytes, but the source has none.
     */
    @Test(expected = EOFException.class)
    public void readDouble_fromEmptySource_shouldThrowEOFException() throws IOException {
        // Arrange: Create a RandomAccessFileOrArray with an empty byte array.
        byte[] emptySourceBytes = new byte[0];
        RandomAccessFileOrArray randomAccess = new RandomAccessFileOrArray(emptySourceBytes);

        // Act & Assert: Attempting to read a double should throw an EOFException.
        randomAccess.readDouble();
    }
}