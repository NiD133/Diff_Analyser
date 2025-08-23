package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that the read(byte[], int, int) method throws an ArrayIndexOutOfBoundsException
     * when the provided offset is outside the bounds of the destination buffer.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void read_shouldThrowException_whenOffsetIsOutOfBounds() throws IOException {
        // Arrange
        // The content of the source data is irrelevant for this test.
        byte[] sourceData = new byte[10];
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(sourceData);

        final int bufferSize = 5;
        byte[] destinationBuffer = new byte[bufferSize];

        // An offset that is clearly larger than the destination buffer's capacity.
        final int invalidOffset = 100;
        final int lengthToRead = 1;

        // Act & Assert
        // This call is expected to throw an ArrayIndexOutOfBoundsException because the offset
        // is invalid for the destinationBuffer.
        fileOrArray.read(destinationBuffer, invalidOffset, lengthToRead);
    }
}