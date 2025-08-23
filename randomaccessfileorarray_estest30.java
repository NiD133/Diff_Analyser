package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that readShort() correctly interprets two bytes as a big-endian short
     * and advances the internal pointer by two positions.
     */
    @Test
    public void readShort_shouldReadTwoBytesAsBigEndianShort_andAdvancePointer() throws IOException {
        // Arrange: Set up the input data and the expected outcome.
        // The readShort() method reads two bytes in big-endian order.
        // The byte array {0x72, 0x00} represents the short value 0x7200.
        byte[] inputBytes = {(byte) 0x72, (byte) 0x00};
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(inputBytes);
        
        short expectedShort = (short) 0x7200; // This is 29184 in decimal
        long expectedPositionAfterRead = 2L;

        // Act: Call the method under test.
        short actualShort = reader.readShort();
        long actualPositionAfterRead = reader.getFilePointer();

        // Assert: Verify that the returned value and the new pointer position are correct.
        assertEquals("The read value should be the big-endian interpretation of the input bytes.", expectedShort, actualShort);
        assertEquals("The file pointer should advance by 2 bytes after reading a short.", expectedPositionAfterRead, actualPositionAfterRead);
    }
}