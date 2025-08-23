package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;

/**
 * This test class contains tests for the RandomAccessFileOrArray class.
 * Note: The original test class name and inheritance are preserved as requested.
 */
public class RandomAccessFileOrArray_ESTestTest80 extends RandomAccessFileOrArray_ESTest_scaffolding {

    /**
     * Verifies that calling readFully() with a null buffer throws a NullPointerException.
     * This ensures the method correctly handles invalid input arguments.
     */
    @Test(timeout = 4000, expected = NullPointerException.class)
    public void readFully_withNullBuffer_shouldThrowNullPointerException() throws IOException {
        // Arrange: Create a RandomAccessFileOrArray instance from a dummy byte array.
        // The content and size of the array are irrelevant for this test, as the
        // null check on the buffer should occur before any data is accessed.
        byte[] sourceBytes = new byte[5];
        RandomAccessFileOrArray randomAccess = new RandomAccessFileOrArray(sourceBytes);

        // Act: Attempt to read data into a null buffer. This call is expected to fail.
        // The offset and length values are arbitrary as the null check happens first.
        randomAccess.readFully(null, 0, 10);

        // Assert: The test passes if the expected NullPointerException is thrown.
        // This is handled declaratively by the 'expected' attribute of the @Test annotation.
    }
}