package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * This class contains tests for the RandomAccessFileOrArray class.
 * The original test class name is kept for context, but in a real-world scenario,
 * it would be renamed to something like RandomAccessFileOrArrayTest.
 */
public class RandomAccessFileOrArray_ESTestTest10 extends RandomAccessFileOrArray_ESTest_scaffolding {

    /**
     * Tests that readDouble() correctly reads 8 bytes from the source,
     * interprets them as a big-endian double, and advances the internal pointer accordingly.
     */
    @Test
    public void readDoubleShouldReturnCorrectValueAndAdvancePointer() throws IOException {
        // Arrange
        // An 8-byte array representing a double in big-endian format.
        // The byte representation corresponds to the long value 0x0000F70000000000L.
        byte[] inputBytes = new byte[8];
        inputBytes[2] = (byte) 0xF7; // This is equivalent to (byte) -9

        // The expected double value is derived from the IEEE 754 representation
        // of the long bits. This makes the connection between the input bytes and
        // the expected result explicit, avoiding a "magic number" in the assertion.
        long longBits = 0x0000F70000000000L;
        double expectedDouble = Double.longBitsToDouble(longBits);

        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(inputBytes);

        // Act
        double actualDouble = reader.readDouble();
        long finalFilePointer = reader.getFilePointer();

        // Assert
        // 1. Verify that the correct double value was read.
        // A delta of 0.0 is used because the bit-level conversion should be exact.
        assertEquals("The read double value should match the expected value.", expectedDouble, actualDouble, 0.0);

        // 2. Verify that the file pointer advanced by 8 bytes (the size of a double).
        assertEquals("The file pointer should advance by 8 after reading a double.", 8L, finalFilePointer);
    }
}