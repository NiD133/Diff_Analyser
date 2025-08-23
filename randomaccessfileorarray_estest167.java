package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

/**
 * Contains tests for the {@link RandomAccessFileOrArray#readFloatLE()} method.
 */
public class RandomAccessFileOrArrayReadFloatLETest {

    /**
     * Verifies that readFloatLE correctly decodes four zero-bytes into the float value 0.0f
     * and advances the internal pointer by four bytes. This tests a fundamental case for
     * reading little-endian float values.
     */
    @Test
    public void readFloatLE_withAllZeroBytes_returnsZeroAndAdvancesPointer() throws IOException {
        // Arrange: A byte array of four zero-bytes, which represents 0.0f in IEEE 754 format.
        // A float is 4 bytes long.
        byte[] inputBytes = new byte[4];
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(inputBytes);

        // Act: Read a little-endian float from the byte array.
        float actualFloat = reader.readFloatLE();

        // Assert: The result should be 0.0f and the pointer should have moved forward by 4.
        final float expectedFloat = 0.0f;
        final long expectedPointerPosition = 4L;

        assertEquals("The decoded float value should be 0.0.", expectedFloat, actualFloat, 0.0f);
        assertEquals("The file pointer should advance by 4 bytes after reading a float.", expectedPointerPosition, reader.getFilePointer());
    }
}