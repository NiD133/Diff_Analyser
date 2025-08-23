package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that readUnsignedIntLE() correctly reads a 32-bit unsigned integer
     * in little-endian byte order and advances the internal pointer by 4 bytes.
     */
    @Test
    public void readUnsignedIntLE_shouldReadFourBytesInLittleEndianOrderAndAdvancePointer() throws IOException {
        // Arrange
        // The test value is 6,160,384, which is 0x005E0000 in hexadecimal.
        // In little-endian byte order, the least significant byte comes first,
        // so the byte representation is [0x00, 0x00, 0x5E, 0x00].
        // We add extra bytes at the end to ensure the method only reads the required four bytes.
        byte[] littleEndianData = {0x00, 0x00, 0x5E, 0x00, (byte) 0xAA, (byte) 0xBB};
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(littleEndianData);

        long expectedValue = 6160384L; // 0x005E0000L
        long expectedPointerPosition = 4L;

        // Act
        long actualValue = reader.readUnsignedIntLE();

        // Assert
        // 1. Verify that the correct numeric value was returned.
        assertEquals("The unsigned integer should be read correctly in little-endian format.", expectedValue, actualValue);

        // 2. Verify that the internal pointer advanced by 4 bytes.
        assertEquals("The file pointer should advance by 4 bytes after reading an int.", expectedPointerPosition, reader.getFilePointer());
    }
}