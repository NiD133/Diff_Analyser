package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that readLongLE() correctly reads an 8-byte long value in
     * little-endian format and advances the internal pointer by 8 bytes.
     */
    @Test
    public void readLongLE_shouldReadLittleEndianLongAndAdvancePointer() throws IOException {
        // Arrange
        // A long is 8 bytes. In little-endian format, the number 16 is represented
        // by the least significant byte having the value 16, followed by 7 zero bytes.
        // The input array is slightly larger to ensure we aren't just reading to the end of the file.
        byte[] littleEndianLongBytes = new byte[] {16, 0, 0, 0, 0, 0, 0, 0, 99};
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(littleEndianLongBytes);

        long expectedValue = 16L;
        long expectedPointerPosition = Long.BYTES; // A long is 8 bytes

        // Act
        long actualValue = reader.readLongLE();

        // Assert
        assertEquals("The method should correctly interpret the little-endian byte order.",
                expectedValue, actualValue);
        assertEquals("The pointer should advance by the number of bytes in a long.",
                expectedPointerPosition, reader.getFilePointer());
    }
}