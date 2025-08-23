package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that readLongLE() correctly reads a long from a source of all zero-bytes.
     * It should return 0L and advance the internal pointer by 8 bytes (the size of a long).
     */
    @Test
    public void readLongLE_fromAllZeroBytes_returnsZeroAndAdvancesPointerBy8() throws IOException {
        // Arrange: Create a source with 8 zero-bytes, which represents the long value 0.
        byte[] inputData = new byte[8];
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(inputData);

        // Act: Read a little-endian long from the source.
        long actualValue = reader.readLongLE();

        // Assert: Verify the correct value was read and the pointer advanced as expected.
        long expectedValue = 0L;
        assertEquals("The method should return 0 when reading from an all-zero byte array.", expectedValue, actualValue);

        long expectedPointerPosition = 8L;
        assertEquals("The file pointer should advance by 8 bytes after reading a long.", expectedPointerPosition, reader.getFilePointer());
    }
}