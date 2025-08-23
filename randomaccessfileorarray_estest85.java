package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that calling readFully() after seeking to a negative position
     * throws an ArrayIndexOutOfBoundsException when the source is a byte array.
     *
     * The seek() operation itself does not validate the position, but the subsequent
     * read operation fails because it attempts to access a negative index in the
     * underlying array.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void readFully_afterSeekingToNegativePosition_throwsArrayIndexOutOfBoundsException() throws IOException {
        // Arrange: Create a RandomAccessFileOrArray backed by a simple byte array.
        byte[] sourceData = new byte[10];
        RandomAccessFileOrArray randomAccessFile = new RandomAccessFileOrArray(sourceData);
        byte[] buffer = new byte[sourceData.length];

        // Act: Seek to an invalid, negative position.
        randomAccessFile.seek(-1L);

        // Assert: Attempting to read from the invalid position should throw an exception.
        // The @Test(expected=...) annotation handles the assertion.
        randomAccessFile.readFully(buffer);
    }
}