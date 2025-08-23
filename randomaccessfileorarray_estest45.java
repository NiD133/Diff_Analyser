package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that readDoubleLE() correctly parses a little-endian double
     * from a byte source and advances the internal pointer by the correct amount.
     */
    @Test
    public void readDoubleLE_givenLittleEndianBytes_returnsCorrectDoubleAndAdvancesPointer() throws IOException {
        // Arrange
        // The double value 1.0 is represented by the 64-bit long 0x3ff0000000000000.
        // In little-endian byte order, this is {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xF0, 0x3F}.
        final double expectedValue = 1.0;
        final byte[] littleEndianBytesForOne = new byte[]{
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xF0, 0x3F
        };
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(littleEndianBytesForOne);

        // Act
        double actualValue = reader.readDoubleLE();

        // Assert
        // 1. Verify that the parsed double value is correct.
        assertEquals("The method should correctly interpret the little-endian byte representation.",
                expectedValue, actualValue, 0.0);

        // 2. Verify that the file pointer has advanced by the size of a double.
        long expectedPosition = Double.BYTES;
        long actualPosition = reader.getFilePointer();
        assertEquals("The file pointer should advance by the size of a double (8 bytes).",
                expectedPosition, actualPosition);
    }
}