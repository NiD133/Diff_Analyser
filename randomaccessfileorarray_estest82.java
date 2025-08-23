package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;

/**
 * This test class contains improved tests for the RandomAccessFileOrArray class.
 * The original tests were auto-generated and have been refactored for clarity.
 */
public class RandomAccessFileOrArray_ESTestTest82 extends RandomAccessFileOrArray_ESTest_scaffolding {

    /**
     * Verifies that the {@link RandomAccessFileOrArray#readFully(byte[], int, int)} method
     * throws an {@link ArrayIndexOutOfBoundsException} when the specified write offset is
     * outside the bounds of the destination buffer.
     *
     * @throws IOException This is part of the method signature but is not expected to be thrown,
     *                     as the exception should occur before any I/O operation.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void readFully_withOffsetOutsideDestinationBounds_shouldThrowException() throws IOException {
        // Arrange: Create a source with some data. The content is not critical for this test,
        // as the error occurs due to invalid arguments before any data is read.
        byte[] sourceData = new byte[10];
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(sourceData);

        // Arrange: Create a destination buffer and define an offset that is clearly
        // outside the buffer's valid index range.
        byte[] destinationBuffer = new byte[5];
        int invalidOffset = 10; // An offset well beyond the buffer's capacity.
        int bytesToRead = 1;    // The number of bytes to read is irrelevant, as the offset check fails first.

        // Act & Assert: Attempt to read into the destination buffer at the invalid offset.
        // The @Test(expected=...) annotation asserts that an ArrayIndexOutOfBoundsException is thrown.
        fileOrArray.readFully(destinationBuffer, invalidOffset, bytesToRead);
    }
}