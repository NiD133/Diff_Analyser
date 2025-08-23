package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.IOException;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that readUnsignedIntLE() correctly reads four bytes from the
     * underlying source and interprets them as an unsigned, little-endian integer.
     */
    @Test
    public void readUnsignedIntLE_shouldReadFourBytesAsLittleEndianUnsignedInt() throws IOException {
        // Arrange: Set up a byte array representing a little-endian unsigned integer.
        // The integer 0x01020304 (16909060 in decimal) is stored in little-endian
        // format as {0x04, 0x03, 0x02, 0x01}. An extra byte is added to ensure
        // the read does not go past the end of the array.
        byte[] inputBytes = new byte[] { 0x04, 0x03, 0x02, 0x01, 0x05 };
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(inputBytes);
        long expectedValue = 16909060L; // This is 0x01020304 in hex

        // Act: Read the unsigned little-endian integer from the byte source.
        long actualValue = reader.readUnsignedIntLE();

        // Assert: Verify the correct value was read and the file pointer advanced by 4 bytes.
        assertEquals("The read value should match the expected little-endian integer.", expectedValue, actualValue);
        assertEquals("The file pointer should advance by 4 bytes after reading an int.", 4L, reader.getFilePointer());
    }
}