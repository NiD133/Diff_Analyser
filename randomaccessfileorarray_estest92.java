package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;

/**
 * This test verifies the behavior of the RandomAccessFileOrArray class
 * when read operations are attempted after the instance has been closed.
 */
public class RandomAccessFileOrArrayClosedStateTest {

    /**
     * Verifies that calling readBoolean() on a closed RandomAccessFileOrArray instance
     * throws a NullPointerException. This is expected because closing the instance
     * nullifies its internal data source.
     */
    @Test(expected = NullPointerException.class)
    public void readBoolean_afterClose_throwsNullPointerException() throws IOException {
        // Arrange: Create an instance from a byte array and then close it.
        byte[] data = new byte[1]; // The content of the array is irrelevant for this test.
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(data);
        fileOrArray.close();

        // Act & Assert: Attempt to read from the closed instance.
        // The @Test(expected) annotation asserts that a NullPointerException is thrown here.
        fileOrArray.readBoolean();
    }
}