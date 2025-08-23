package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that readByte() correctly reads the first byte from a source byte array
     * and advances the internal file pointer accordingly.
     */
    @Test
    public void readByte_whenBackedByArray_readsFirstByteAndAdvancesPointer() throws IOException {
        // Arrange: Create a RandomAccessFileOrArray instance backed by a byte array with known content.
        byte[] sourceData = {15, 25, 35, 45, 55};
        RandomAccessFileOrArray randomAccess = new RandomAccessFileOrArray(sourceData);

        byte expectedByte = 15;
        long expectedPointerPosition = 1L;

        // Act: Read a single byte from the source.
        byte actualByte = randomAccess.readByte();
        long actualPointerPosition = randomAccess.getFilePointer();

        // Assert: Verify that the correct byte was returned and the pointer was advanced by one.
        assertEquals("The method should return the first byte from the source array.", expectedByte, actualByte);
        assertEquals("The file pointer should advance by one byte after reading.", expectedPointerPosition, actualPointerPosition);
    }
}