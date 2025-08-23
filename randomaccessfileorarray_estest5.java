package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class, focusing on sequential read operations.
 */
public class RandomAccessFileOrArray_ESTestTest5 extends RandomAccessFileOrArray_ESTest_scaffolding {

    /**
     * Verifies that calling readShortLE() followed by readUnsignedInt() reads the correct
     * values and correctly advances the internal file pointer.
     */
    @Test
    public void readShortLEThenReadUnsignedInt_advancesPositionAndReadsCorrectValues() throws IOException {
        // Arrange
        // This test verifies a sequence of reads:
        // 1. A 2-byte little-endian short.
        // 2. A 4-byte big-endian unsigned int.
        //
        // Byte layout:
        // Index 0-1: Little-endian short (0x0000 -> 0)
        // Index 2-5: Big-endian unsigned int (0xF7340000 -> 4147380224L)
        // Index 6-7: Padding bytes, not used in this test.
        byte[] inputData = new byte[]{
                // Bytes for the short value 0 (LE: 0x00, 0x00)
                0x00, 0x00,
                // Bytes for the unsigned int value 4147380224 (BE: 0xF7, 0x34, 0x00, 0x00)
                (byte) 0xF7, (byte) 0x34, 0x00, 0x00,
                // Padding
                0x00, 0x00
        };

        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(inputData);

        final short expectedShort = 0;
        final long expectedUnsignedInt = 4147380224L; // Hex: 0xF7340000L

        // Act
        // Read the 2-byte little-endian short, which should advance the position to index 2.
        short actualShort = reader.readShortLE();

        // From the new position, read the 4-byte big-endian unsigned int.
        long actualUnsignedInt = reader.readUnsignedInt();

        // Assert
        assertEquals("The little-endian short should be read correctly.", expectedShort, actualShort);
        assertEquals("The big-endian unsigned int should be read correctly after the first read.", expectedUnsignedInt, actualUnsignedInt);
    }
}