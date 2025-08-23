package com.itextpdf.text.io;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

/**
 * Contains tests for the {@link GetBufferedRandomAccessSource} class.
 */
public class GetBufferedRandomAccessSourceTest {

    /**
     * Verifies that the get() method returns -1 when a negative length is requested.
     * According to the contract for read/get methods, a negative length is an invalid
     * argument, and in this context, returning -1 indicates that no bytes were read,
     * which is a sensible failure mode.
     */
    @Test
    public void get_withNegativeLength_shouldReturnMinusOne() throws IOException {
        // Arrange
        // The content of the source is irrelevant for this test case, so an empty source is sufficient.
        RandomAccessSource source = new ArrayRandomAccessSource(new byte[0]);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(source);

        byte[] destinationBuffer = new byte[10];
        long readPosition = 0L;
        int bufferOffset = 0;
        int negativeLength = -1; // The invalid argument being tested.

        // Act
        int bytesRead = bufferedSource.get(readPosition, destinationBuffer, bufferOffset, negativeLength);

        // Assert
        assertEquals("The get method should return -1 to indicate an invalid read length.", -1, bytesRead);
    }
}