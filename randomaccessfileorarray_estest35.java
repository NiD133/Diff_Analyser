package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class, focusing on data reading methods.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that readLong() correctly reads an 8-byte long from a byte array,
     * returns the expected value (0 for a zeroed array), and advances the internal pointer by 8 bytes.
     */
    @Test
    public void readLong_readsEightBytesAndAdvancesPointerCorrectly() throws IOException {
        // Arrange: Create a data source with all zero bytes. A long is 8 bytes, so the
        // array must be at least that long. We use a size of 16 to ensure the
        // implementation doesn't rely on the source being exactly 8 bytes.
        byte[] inputData = new byte[16];
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(inputData);

        // Act: Read a long value from the start of the data source.
        long actualValue = reader.readLong();

        // Assert: Verify both the returned value and the new pointer position.
        long expectedValue = 0L;
        assertEquals("The long value read from a zeroed array should be 0.", expectedValue, actualValue);

        long expectedPointerPosition = 8L; // A long consists of 8 bytes.
        assertEquals("The file pointer should advance by 8 bytes after reading a long.", expectedPointerPosition, reader.getFilePointer());
    }
}