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
     * Verifies that readFloatLE() correctly reads four bytes from the input source,
     * interprets them as a little-endian float, and advances the file pointer by four bytes.
     */
    @Test
    public void readFloatLE_shouldReadLittleEndianFloatAndAdvancePointer() throws IOException {
        // Arrange
        // A float is 4 bytes. We can define a float by its integer bit representation.
        // The integer 4096 is represented in little-endian byte order as [0, 16, 0, 0].
        // Calculation: 4096 = 16 * 256. In hex, this is 0x1000.
        // The little-endian bytes are therefore [0x00, 0x10, 0x00, 0x00].
        byte[] littleEndianFloatBytes = new byte[]{0, 16, 0, 0};

        // The expected float is the value corresponding to the integer bit representation of 4096.
        // This makes the relationship between the input and expected output explicit.
        float expectedFloat = Float.intBitsToFloat(4096);
        final long BYTES_IN_FLOAT = 4L;

        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(new ByteArrayInputStream(littleEndianFloatBytes));

        // Act
        float actualFloat = reader.readFloatLE();
        long finalFilePointer = reader.getFilePointer();

        // Assert
        // 1. Verify that the read float matches the expected value.
        //    A delta of 0.0f is used because the conversion should be exact.
        assertEquals("The float value should be read correctly in little-endian format.", expectedFloat, actualFloat, 0.0f);

        // 2. Verify that the file pointer has advanced by the size of a float.
        assertEquals("The file pointer should advance by 4 bytes.", BYTES_IN_FLOAT, finalFilePointer);
    }
}