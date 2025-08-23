package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

public class RandomAccessFileOrArray_ESTestTest134 extends RandomAccessFileOrArray_ESTest_scaffolding {

    /**
     * Tests that readUnsignedIntLE() correctly reads a 4-byte little-endian
     * unsigned integer and advances the file pointer by four bytes.
     */
    @Test
    public void readUnsignedIntLE_shouldReadFourBytesAndAdvancePointer() throws IOException {
        // Arrange: Create a byte array representing a little-endian unsigned int.
        // The value 0x04030201 is represented as {1, 2, 3, 4} in little-endian format.
        // An extra byte {99} is added to ensure we don't read past the integer.
        byte[] sourceData = {1, 2, 3, 4, 99};
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(sourceData);

        assertEquals("Initial file pointer should be at the beginning", 0L, reader.getFilePointer());

        // Act: Read the unsigned little-endian integer from the source.
        long result = reader.readUnsignedIntLE();

        // Assert: Verify the correct value was read and the pointer was advanced.
        long expectedValue = 0x04030201L; // This is 67,305,985 in decimal.
        assertEquals("The little-endian unsigned int should be read correctly", expectedValue, result);
        assertEquals("File pointer should advance by 4 bytes after reading", 4L, reader.getFilePointer());
    }
}