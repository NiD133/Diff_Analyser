package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class.
 */
// The original test class name and hierarchy are preserved.
public class RandomAccessFileOrArray_ESTestTest43 extends RandomAccessFileOrArray_ESTest_scaffolding {

    /**
     * Verifies that readFloat() correctly uses a pushed-back byte as the first byte
     * of the 4-byte float value and updates the file pointer accordingly.
     */
    @Test
    public void readFloat_whenByteIsPushedBack_usesPushedBackByteAsFirstByte() throws IOException {
        // Arrange
        // The source data contains enough bytes for the read operation after the pushed-back byte is consumed.
        byte[] sourceData = {0, 0, 0, 99}; // The last byte (99) is extra and should not be read.
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(sourceData);

        byte pushedBackByte = 118; // This will be the most significant byte of the float.
        reader.pushBack(pushedBackByte);

        // The float should be constructed from the pushed-back byte (118) followed by the
        // first three bytes from the source array {0, 0, 0}.
        // The resulting 4-byte sequence in big-endian is {118, 0, 0, 0}.
        // This corresponds to the integer representation 0x76000000.
        int expectedFloatBits = (pushedBackByte & 0xFF) << 24;
        float expectedFloat = Float.intBitsToFloat(expectedFloatBits);

        // Act
        float actualFloat = reader.readFloat();

        // Assert
        // 1. Verify the float value is correct. A delta of 0.0f ensures an exact match.
        assertEquals(
            "The read float value should be constructed from the pushed-back byte and subsequent source bytes.",
            expectedFloat,
            actualFloat,
            0.0f
        );

        // 2. Verify the file pointer has advanced by 3 positions in the source array.
        //    readFloat() consumes 4 bytes total, but one came from the pushback buffer,
        //    so only 3 were read from the underlying source.
        long expectedPosition = 3L;
        assertEquals(
            "File pointer should advance by the number of bytes read from the source, not including the pushed-back byte.",
            expectedPosition,
            reader.getFilePointer()
        );
    }
}