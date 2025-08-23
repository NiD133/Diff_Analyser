package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that readFloatLE() correctly parses a 4-byte little-endian sequence
     * into a float and advances the internal pointer by four bytes.
     */
    @Test
    public void readFloatLE_shouldCorrectlyParseLittleEndianFloatAndAdvancePointer() throws IOException {
        // --- Arrange ---
        // The 4-byte sequence [0x00, 0x00, 0x00, 0xFF] represents the integer 0xFF000000
        // when read in little-endian order. This test verifies that the method correctly
        // interprets these bits as a floating-point number.
        byte[] inputBytes = new byte[] {0, 0, 0, (byte) 0xFF};

        // The expected float is derived from the integer representation of the little-endian bytes.
        // This makes the test self-documenting and avoids "magic" float values.
        int intBitsRepresentation = 0xFF000000;
        float expectedFloat = Float.intBitsToFloat(intBitsRepresentation);

        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(new ByteArrayInputStream(inputBytes));

        // --- Act ---
        float actualFloat = reader.readFloatLE();

        // --- Assert ---
        // 1. Verify the parsed float value is correct.
        // An exact comparison (delta = 0.0f) is appropriate here as we are testing a direct bit-level conversion.
        assertEquals("The little-endian float value should be parsed correctly.", expectedFloat, actualFloat, 0.0f);

        // 2. Verify the file pointer advanced by the size of a float (4 bytes).
        long expectedFilePointer = 4L;
        long actualFilePointer = reader.getFilePointer();
        assertEquals("The file pointer should advance by 4 bytes after reading a float.", expectedFilePointer, actualFilePointer);
    }
}