package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that readByte() correctly reads a single byte from the source
     * and advances the internal file pointer by one.
     */
    @Test
    public void readByte_shouldReturnFirstByteAndAdvancePointer() throws IOException {
        // Arrange: Create a data source with a specific first byte.
        // We test with a negative value to ensure signed bytes are handled correctly.
        final byte expectedByte = (byte) -124;
        byte[] sourceData = {expectedByte, 10, 20, 30};
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(sourceData);

        // Act: Read a single byte from the source.
        byte actualByte = reader.readByte();

        // Assert: Verify that the correct byte was read and the pointer was advanced.
        assertEquals("The byte read from the source should match the expected value.", expectedByte, actualByte);
        assertEquals("The file pointer should be advanced by 1 after reading a byte.", 1L, reader.getFilePointer());
    }
}