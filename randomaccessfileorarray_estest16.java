package com.itextpdf.text.pdf;

import org.junit.Test;

import java.io.EOFException;
import java.io.IOException;

/**
 * Test suite for the {@link RandomAccessFileOrArray} class, focusing on read operations.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that attempting to read an unsigned short beyond the end of the
     * underlying data source correctly throws an EOFException.
     */
    @Test(expected = EOFException.class)
    public void readUnsignedShort_whenReadingPastEndOfData_throwsEOFException() throws IOException {
        // Arrange: Create a data source with exactly 2 bytes, the size of a short.
        byte[] data = new byte[2];
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(data);

        // Act:
        // 1. Consume all the data in the source by reading a short (2 bytes).
        // This advances the internal pointer to the end of the data.
        fileOrArray.readShort();

        // 2. Attempt to read another unsigned short from the now-empty stream.
        // This action is expected to throw the EOFException.
        fileOrArray.readUnsignedShort();

        // Assert: The test succeeds if an EOFException is thrown, as declared
        // by the @Test(expected) annotation. If no exception is thrown, the
        // test will fail automatically.
    }
}