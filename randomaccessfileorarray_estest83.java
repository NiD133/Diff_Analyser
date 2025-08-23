package com.itextpdf.text.pdf;

import org.junit.Test;

import java.io.IOException;

/**
 * Test suite for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that the {@link RandomAccessFileOrArray#readFully(byte[])} method
     * throws a {@link NullPointerException} when a null buffer is provided.
     */
    @Test(expected = NullPointerException.class)
    public void readFully_withNullBuffer_shouldThrowNullPointerException() throws IOException {
        // Arrange: Create an instance of RandomAccessFileOrArray with dummy data.
        byte[] sourceData = new byte[9];
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(sourceData);

        // Act: Attempt to read into a null buffer.
        // The test framework will assert that a NullPointerException is thrown.
        fileOrArray.readFully(null);
    }
}