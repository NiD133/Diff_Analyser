package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArray_ESTestTest97 {

    /**
     * Verifies that attempting to read from a RandomAccessFileOrArray instance
     * after it has been closed results in an IllegalStateException.
     */
    @Test
    public void readAfterCloseThrowsIllegalStateException() throws IOException {
        // Arrange: Create a RandomAccessFileOrArray from a byte array and then close it.
        byte[] buffer = new byte[1]; // The buffer content is irrelevant for this test.
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(buffer);
        fileOrArray.close();

        // Act & Assert: Attempt to read from the closed instance and verify the exception.
        try {
            fileOrArray.read(buffer);
            fail("Expected an IllegalStateException to be thrown when reading from a closed source.");
        } catch (IllegalStateException e) {
            // Verify that the correct exception was thrown with the expected message.
            assertEquals("Already closed", e.getMessage());
        }
    }
}