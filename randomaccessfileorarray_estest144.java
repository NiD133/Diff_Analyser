package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link RandomAccessFileOrArray} class, focusing on data reading methods.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that readUnsignedShortLE() correctly reads a 16-bit unsigned integer
     * in little-endian byte order and advances the internal pointer by two bytes.
     */
    @Test
    public void readUnsignedShortLE_readsLittleEndianValueAndAdvancesPointer() throws IOException {
        // Arrange: Create a byte array representing the number 0x1234 (4660) in little-endian format.
        // The bytes are [0x34, 0x12]. An extra byte is added to ensure only two are read.
        byte[] inputBytes = new byte[] { 0x34, 0x12, 0x56 };
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(inputBytes);
        int expectedValue = 0x1234; // 4660 in decimal

        // Act: Read an unsigned short in little-endian format.
        int actualValue = reader.readUnsignedShortLE();

        // Assert: Verify that the correct value was read and the file pointer advanced appropriately.
        assertEquals("The unsigned short value should be read correctly in little-endian format.", expectedValue, actualValue);
        assertEquals("The file pointer should advance by 2 bytes after reading a short.", 2L, reader.getFilePointer());
    }
}