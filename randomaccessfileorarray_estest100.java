package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;

/**
 * Test suite for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that calling the length() method on a closed RandomAccessFileOrArray instance
     * results in a NullPointerException. This ensures that the object cannot be used
     * after its resources have been released.
     */
    @Test(expected = NullPointerException.class)
    public void lengthShouldThrowNullPointerExceptionWhenCalledAfterClose() throws IOException {
        // Arrange: Create a RandomAccessFileOrArray from a byte array.
        byte[] sourceData = new byte[10];
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(sourceData);

        // Act: Close the instance, which should release its internal resources.
        fileOrArray.close();

        // Assert: Attempting to get the length of the closed instance is expected to throw.
        // The @Test(expected) annotation handles the exception assertion.
        fileOrArray.length();
    }
}