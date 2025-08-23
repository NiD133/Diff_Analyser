package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;

/**
 * Unit tests for the {@link RandomAccessFileOrArray} class, focusing on its behavior after being closed.
 */
public class RandomAccessFileOrArrayClosedStateTest {

    /**
     * Verifies that attempting to read from a RandomAccessFileOrArray after it has been closed
     * results in a NullPointerException.
     *
     * This test ensures that the resource cleanup in the close() method correctly invalidates
     * the object's state, preventing further read operations.
     */
    @Test(expected = NullPointerException.class)
    public void readIntLE_onClosedStream_throwsNullPointerException() throws IOException {
        // Arrange: Create a RandomAccessFileOrArray instance and immediately close it.
        // The content of the byte array is irrelevant for this test.
        byte[] dummyData = new byte[4];
        RandomAccessFileOrArray randomAccessData = new RandomAccessFileOrArray(dummyData);
        randomAccessData.close();

        // Act & Assert: Attempting to read from the closed instance should throw a NullPointerException.
        // The @Test(expected) annotation handles the assertion.
        randomAccessData.readIntLE();
    }
}