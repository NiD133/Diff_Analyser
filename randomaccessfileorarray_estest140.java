package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that readCharLE() correctly reads two null bytes as a little-endian character ('\u0000')
     * and advances the internal pointer by two bytes.
     */
    @Test
    public void readCharLE_withTwoNullBytes_returnsNullCharAndAdvancesPointer() throws IOException {
        // Arrange
        // A char is 2 bytes. We provide extra bytes to ensure only two are read.
        byte[] inputBytes = new byte[]{0, 0, 1, 2, 3};
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(inputBytes);

        char expectedChar = '\u0000';
        long expectedPointerPosition = 2L;

        // Act
        char actualChar = reader.readCharLE();
        long actualPointerPosition = reader.getFilePointer();

        // Assert
        assertEquals("The method should correctly interpret two null bytes as a null character.", expectedChar, actualChar);
        assertEquals("The file pointer should advance by 2 bytes after reading a char.", expectedPointerPosition, actualPointerPosition);
    }
}