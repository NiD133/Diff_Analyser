package com.itextpdf.text.pdf;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * This test class name, RandomAccessFileOrArray_ESTestTest95, is auto-generated.
 * A more descriptive name, such as RandomAccessFileOrArrayTest, is recommended for clarity.
 */
public class RandomAccessFileOrArray_ESTestTest95 {

    /**
     * Verifies that attempting to read from a RandomAccessFileOrArray after it has been closed
     * results in an IllegalStateException.
     */
    @Test
    public void read_shouldThrowIllegalStateException_whenSourceIsClosed() throws IOException {
        // Arrange: Create a RandomAccessFileOrArray and immediately close it to set up the test condition.
        byte[] sourceData = new byte[10];
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(sourceData);
        fileOrArray.close();

        // Act & Assert: Attempt to read from the closed source and verify the expected exception.
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> fileOrArray.read(sourceData, 0, sourceData.length)
        );

        // Assert: Check that the exception message clearly indicates the reason for failure.
        assertEquals("Already closed", exception.getMessage());
    }
}