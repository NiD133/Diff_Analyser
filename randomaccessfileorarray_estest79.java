package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class, focusing on its behavior after being closed.
 */
public class RandomAccessFileOrArrayClosedStateTest {

    /**
     * Verifies that attempting to read from a RandomAccessFileOrArray instance after it has been closed
     * results in a NullPointerException. This ensures that the object correctly invalidates its state
     * upon closure, preventing subsequent I/O operations.
     */
    @Test(expected = NullPointerException.class)
    public void readInt_onClosedInstance_shouldThrowNullPointerException() throws IOException {
        // Arrange: Create an instance from a byte array and immediately close it.
        byte[] dummyData = { 0x01, 0x02, 0x03, 0x04 };
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(dummyData);
        fileOrArray.close();

        // Act: Attempt to read an integer from the closed instance.
        // The test framework will assert that a NullPointerException is thrown here.
        fileOrArray.readInt();
    }
}