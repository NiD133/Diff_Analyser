package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class, focusing on data reading methods.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that readUnsignedShortLE() correctly reads two bytes in little-endian
     * order and advances the internal pointer.
     */
    @Test
    public void readUnsignedShortLE_shouldReadTwoBytesInLittleEndianOrderAndAdvancePointer() throws IOException {
        // Arrange
        // The two bytes to be read are 0xF9 (least significant byte) and 0x0D (most significant byte).
        // The original test used (byte)-7, which is equivalent to 0xF9. Using hex makes the byte value clearer.
        byte[] sourceData = new byte[] { (byte) 0xF9, (byte) 0x0D, 0x00, 0x00 };
        RandomAccessFileOrArray randomAccess = new RandomAccessFileOrArray(sourceData);

        // In little-endian format, the value is calculated as (MSB << 8) | LSB.
        // So, 0x0DF9 = (0x0D * 256) + 0xF9 = (13 * 256) + 249 = 3577.
        int expectedValue = 3577;
        long expectedPointerPositionAfterRead = 2L;

        // Act
        int actualValue = randomAccess.readUnsignedShortLE();
        long actualPointerPosition = randomAccess.getFilePointer();

        // Assert
        assertEquals("The unsigned short value should be read correctly in little-endian format.", expectedValue, actualValue);
        assertEquals("The file pointer should advance by two bytes after the read operation.", expectedPointerPositionAfterRead, actualPointerPosition);
    }
}