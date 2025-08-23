package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class, focusing on reading numerical data.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that readShortLE() correctly reads a 16-bit short integer in
     * little-endian byte order and advances the internal pointer by two bytes.
     */
    @Test
    public void readShortLE_shouldReadCorrectValue_andAdvancePointerByTwo() throws IOException {
        // Arrange: Prepare the input data and expected outcomes.
        // The short value 47 is represented in 16 bits as 0x002F.
        // In little-endian format, the least significant byte (0x2F = 47) comes first.
        byte[] sourceData = new byte[]{ 47, 0, (byte) 0xFF, (byte) 0xFF }; // Extra bytes to ensure only two are read
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(sourceData);

        final short EXPECTED_VALUE = 47;
        final long EXPECTED_POINTER_POSITION = 2L;

        // Act: Call the method under test.
        short result = reader.readShortLE();

        // Assert: Verify the result and the side effect (pointer movement).
        assertEquals("The method should correctly interpret the little-endian bytes.",
                EXPECTED_VALUE, result);

        assertEquals("The file pointer should advance by 2 bytes after reading a short.",
                EXPECTED_POINTER_POSITION, reader.getFilePointer());
    }
}