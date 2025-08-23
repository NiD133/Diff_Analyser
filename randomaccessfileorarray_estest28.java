package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

public class RandomAccessFileOrArray_ESTestTest28 extends RandomAccessFileOrArray_ESTest_scaffolding {

    /**
     * Tests that readShortLE() correctly parses a 2-byte little-endian value
     * and advances the internal pointer accordingly.
     */
    @Test
    public void readShortLEShouldCorrectlyParseLittleEndianValueAndAdvancePointer() throws IOException {
        // Arrange
        // The short value 4096 is represented as 0x1000 in hexadecimal.
        // In little-endian byte order, the least significant byte (0x00) comes first,
        // followed by the most significant byte (0x10).
        byte[] littleEndianBytes = {(byte) 0x00, (byte) 0x10};
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(littleEndianBytes);

        final short expectedValue = 4096;
        final long expectedPointerPosition = 2L;

        // Act
        short actualValue = reader.readShortLE();
        long actualPointerPosition = reader.getFilePointer();

        // Assert
        assertEquals("The little-endian short value was not read correctly.", expectedValue, actualValue);
        assertEquals("The file pointer should advance by 2 bytes after reading a short.", expectedPointerPosition, actualPointerPosition);
    }
}