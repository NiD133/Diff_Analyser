package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that attempting to read from a RandomAccessFileOrArray instance after it has been closed
     * results in a NullPointerException. This is the expected behavior, as closing the instance
     * should release its internal resources.
     */
    @Test(expected = NullPointerException.class)
    public void readUnsignedIntLE_afterClose_throwsNullPointerException() throws IOException {
        // Arrange: Create an instance from a byte array and then close it.
        byte[] sourceData = new byte[16]; // The content and size are not important for this test.
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(sourceData);
        fileOrArray.close();

        // Act: Attempt to read from the closed instance.
        // This action is expected to throw a NullPointerException.
        fileOrArray.readUnsignedIntLE();

        // Assert: The 'expected' attribute of the @Test annotation handles the exception verification.
        // The test will pass only if a NullPointerException is thrown.
    }
}