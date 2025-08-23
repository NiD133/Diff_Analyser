package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class, focusing on invalid argument handling.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that calling readString() with a negative length
     * throws a NegativeArraySizeException.
     * <p>
     * This is the expected behavior because the method likely attempts to allocate a
     * byte array using the provided length, and Java does not permit arrays with a
     * negative size.
     */
    @Test(expected = NegativeArraySizeException.class)
    public void readStringWithNegativeLengthThrowsException() throws IOException {
        // Arrange: Create an instance of RandomAccessFileOrArray with dummy data.
        // The content of the byte array is irrelevant for this test case.
        byte[] dummyData = new byte[10];
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(dummyData);

        int invalidLength = -1;
        String anyEncoding = "UTF-8";

        // Act: Attempt to read a string with a negative length.
        // This action is expected to throw a NegativeArraySizeException.
        fileOrArray.readString(invalidLength, anyEncoding);

        // Assert: The test passes if the expected NegativeArraySizeException is thrown.
        // This is handled declaratively by the @Test(expected=...) annotation.
    }
}