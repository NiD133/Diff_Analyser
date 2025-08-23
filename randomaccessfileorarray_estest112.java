package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that the file pointer is at the beginning (position 0)
     * immediately after creating a RandomAccessFileOrArray instance from a byte array.
     */
    @Test
    public void getFilePointer_shouldReturnZero_whenNewlyCreatedFromArray() throws IOException {
        // Arrange: Create a RandomAccessFileOrArray from a non-empty byte array.
        byte[] sourceData = new byte[5];
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(sourceData);
        long expectedPosition = 0L;

        // Act: Get the current position of the file pointer.
        long actualPosition = fileOrArray.getFilePointer();

        // Assert: The position should be at the start of the data.
        assertEquals("The initial file pointer should be at position 0.", expectedPosition, actualPosition);
    }
}