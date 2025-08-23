package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.EOFException;
import java.io.IOException;

/**
 * Test suite for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that attempting to read a byte from an empty data source
     * correctly throws an End-Of-File (EOF) exception.
     */
    @Test(expected = EOFException.class)
    public void readByte_fromEmptySource_throwsEOFException() throws IOException {
        // Arrange: Create a RandomAccessFileOrArray with an empty byte array as its source.
        byte[] emptySource = new byte[0];
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(emptySource);

        // Act: Attempt to read a byte from the empty source.
        // This action is expected to throw the exception.
        fileOrArray.readByte();

        // Assert: The test expects an EOFException to be thrown, which is
        // handled declaratively by the @Test(expected = ...) annotation.
    }
}