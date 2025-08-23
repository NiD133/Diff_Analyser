package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class.
 * This class name is improved from the auto-generated "RandomAccessFileOrArray_ESTestTest9".
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Tests that readLong() correctly reads an 8-byte, big-endian long value
     * and advances the file pointer by 8 positions.
     */
    @Test
    public void readLong_shouldReadBigEndianLongAndAdvancePointer() throws IOException {
        // Arrange: Create a byte array representing a long in big-endian format.
        // The byte at index 3 is 52 (0x34), and all others are 0.
        // This corresponds to the long value 52 * 2^32.
        byte[] inputBytes = {
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x34,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
        };
        // The expected long value is 0x3400000000L, which is 223338299392 in decimal.
        long expectedValue = 0x3400000000L;
        RandomAccessFileOrArray randomAccess = new RandomAccessFileOrArray(new ByteArrayInputStream(inputBytes));

        // Act: Read a long from the stream.
        long actualValue = randomAccess.readLong();
        long finalFilePointer = randomAccess.getFilePointer();

        // Assert: Verify the read value and the new file pointer position.
        assertEquals("The read long value should match the expected big-endian interpretation.",
                     expectedValue, actualValue);
        assertEquals("The file pointer should advance by 8 bytes (the size of a long).",
                     8L, finalFilePointer);
    }
}