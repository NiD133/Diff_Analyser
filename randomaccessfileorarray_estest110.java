package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Contains unit tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that readInt() correctly reads a 4-byte integer from a byte array
     * of zeros and advances the internal pointer by four bytes.
     */
    @Test
    public void readInt_fromByteArrayOfZeros_returnsZeroAndAdvancesPointer() throws IOException {
        // Arrange: Create a source with 5 bytes, all initialized to zero.
        // An int is 4 bytes, so a 5-byte array ensures there's extra data past the read.
        // The default value for a new byte array is 0 for all elements.
        byte[] sourceBytes = new byte[5];
        RandomAccessFileOrArray randomAccess = new RandomAccessFileOrArray(sourceBytes);

        // Act: Read an integer from the beginning of the source.
        int result = randomAccess.readInt();

        // Assert: Check the read value and the new pointer position.
        // The integer represented by four zero bytes {0, 0, 0, 0} is 0.
        assertEquals("The integer read from four zero-bytes should be 0.", 0, result);

        // The pointer should advance by 4 bytes (the size of an int).
        long expectedPosition = 4L;
        long actualPosition = randomAccess.getFilePointer();
        assertEquals("The file pointer should advance by 4 bytes after reading an int.", expectedPosition, actualPosition);
    }
}