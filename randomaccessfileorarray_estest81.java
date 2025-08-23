package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.IOException;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that attempting to read from a RandomAccessFileOrArray instance
     * after it has been closed results in an IllegalStateException.
     */
    @Test
    public void readFully_whenClosed_throwsIllegalStateException() throws IOException {
        // Arrange: Create a RandomAccessFileOrArray from a byte array and then close it
        // to set up the state for the test.
        byte[] sourceData = new byte[16];
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(sourceData);
        fileOrArray.close();

        // Act & Assert: Attempt to read from the closed instance and verify that the
        // correct exception is thrown.
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> fileOrArray.readFully(new byte[1], 0, 1)
        );

        // Assert: Further verify that the exception message is as expected.
        assertEquals("Already closed", exception.getMessage());
    }
}