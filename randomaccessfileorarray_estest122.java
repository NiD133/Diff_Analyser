package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;

/**
 * Contains tests for the {@link RandomAccessFileOrArray#read(byte[], int, int)} method.
 */
public class RandomAccessFileOrArrayReadTest {

    /**
     * Verifies that the read(byte[], int, int) method throws an ArrayIndexOutOfBoundsException
     * if the combination of the offset and length parameters would cause a write
     * past the end of the destination buffer.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void read_whenWritingPastEndOfDestinationBuffer_throwsException() throws IOException {
        // Arrange: Create a source with enough data to fulfill the read request.
        byte[] sourceData = new byte[10];
        RandomAccessFileOrArray randomAccessFileOrArray = new RandomAccessFileOrArray(sourceData);

        // Arrange: Define a destination buffer and read parameters that will cause an overflow.
        // The destination buffer has a size of 14.
        byte[] destinationBuffer = new byte[14];
        int offset = 8;
        int lengthToRead = 8;

        // Act: Attempt to read data into the destination buffer.
        // This should fail because writing 8 bytes at an offset of 8 requires a
        // buffer of at least size 16 (8 + 8), but the buffer is only size 14.
        randomAccessFileOrArray.read(destinationBuffer, offset, lengthToRead);

        // Assert: The test expects an ArrayIndexOutOfBoundsException, which is
        // handled by the @Test(expected=...) annotation.
    }
}