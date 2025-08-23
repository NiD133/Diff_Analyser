package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class, focusing on integer reading.
 */
// The original test class name is preserved for context, though in a real-world scenario,
// it would be renamed to something like RandomAccessFileOrArrayTest.
public class RandomAccessFileOrArray_ESTestTest6 extends RandomAccessFileOrArray_ESTest_scaffolding {

    /**
     * Verifies that readUnsignedInt() correctly reads a 4-byte, big-endian integer
     * from a byte array and advances the internal pointer accordingly.
     */
    @Test
    public void readUnsignedInt_shouldReadBigEndianIntegerAndAdvancePointer() throws IOException {
        // Arrange
        // An unsigned int is 4 bytes. The value 52 in big-endian is represented as {0, 0, 0, 52}.
        // We add extra bytes at the end to ensure the reader processes only the first four bytes.
        byte[] sourceData = {0, 0, 0, 52, (byte) 0xFF, (byte) 0xFF};
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(sourceData);

        long expectedValue = 52L;
        long expectedPositionAfterRead = 4L;

        // Act
        long actualValue = reader.readUnsignedInt();
        long actualPositionAfterRead = reader.getFilePointer();

        // Assert
        assertEquals("The read unsigned integer should match the expected value.", expectedValue, actualValue);
        assertEquals("The file pointer should advance by 4 bytes after the read.", expectedPositionAfterRead, actualPositionAfterRead);
    }
}