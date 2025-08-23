package com.itextpdf.text.pdf;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test suite for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that the read(byte[], int, int) method throws a NullPointerException
     * when the provided buffer is null. This is a standard contract for read methods
     * in Java I/O that should be upheld.
     */
    @Test(expected = NullPointerException.class)
    public void readWithNullBufferThrowsNullPointerException() throws IOException {
        // Arrange: Create a RandomAccessFileOrArray from a dummy input stream.
        // The content of the stream is irrelevant for this test.
        byte[] dummyData = new byte[10];
        InputStream inputStream = new ByteArrayInputStream(dummyData);
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(inputStream);

        // Act & Assert: Attempt to read into a null buffer.
        // The method should throw a NullPointerException before attempting to read.
        // The offset and length values are arbitrary as the null check happens first.
        fileOrArray.read(null, 0, 1);
    }
}