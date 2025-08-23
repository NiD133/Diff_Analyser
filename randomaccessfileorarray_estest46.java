package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link RandomAccessFileOrArray#readDoubleLE()} method.
 */
public class RandomAccessFileOrArrayReadDoubleLETest {

    /**
     * Verifies that readDoubleLE correctly interprets an 8-byte array
     * in little-endian format and converts it to a double value.
     * It also confirms that the internal pointer advances by 8 bytes after the read.
     */
    @Test
    public void readDoubleLE_shouldCorrectlyParseLittleEndianBytes() throws IOException {
        // Arrange: Create a byte array representing a double in little-endian format.
        // The byte sequence [0x00, 0x00, 0xF7, 0x00, 0x00, 0x00, 0x00, 0x00]
        // corresponds to the 64-bit integer value 0x0000000000F70000L.
        byte[] littleEndianBytes = new byte[] {
            0x00, 0x00, (byte) 0xF7, 0x00, 0x00, 0x00, 0x00, 0x00
        };
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(littleEndianBytes);

        // To make the test's intent clear and self-documenting, we calculate the
        // expected double value from its known 64-bit integer representation.
        long longBits = 0x00F70000L;
        double expectedDouble = Double.longBitsToDouble(longBits); // Should be ~7.9976343E-317

        // Act: Read the double value from the byte array source.
        double actualDouble = reader.readDoubleLE();

        // Assert: Verify the parsed value and the new pointer position.
        // The delta is 0.0 because the bit-level conversion should be exact.
        assertEquals("The double value should be parsed correctly from little-endian bytes.", expectedDouble, actualDouble, 0.0);
        assertEquals("The pointer should advance by 8 bytes (the size of a double).", 8L, reader.getFilePointer());
    }
}