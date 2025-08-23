package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link RandomAccessFileOrArray#readFloat()} method.
 */
public class RandomAccessFileOrArrayReadFloatTest {

    /**
     * Verifies that reading a float from a source of zero-bytes correctly returns 0.0f
     * and advances the file pointer by the size of a float.
     */
    @Test
    public void readFloat_fromZeroByteArray_returnsZeroAndAdvancesPointer() throws IOException {
        // Arrange
        // A byte array containing all zeros, which represents 0.0f in IEEE 754 format.
        // The array is made longer than a float to ensure there's enough data to read.
        byte[] sourceBytes = new byte[8];
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(sourceBytes);

        // Act
        float actualFloat = fileOrArray.readFloat();
        long finalFilePointer = fileOrArray.getFilePointer();

        // Assert
        assertEquals("Reading four zero-bytes should result in 0.0f.", 0.0f, actualFloat, 0.0f);
        assertEquals("File pointer should advance by the size of a float.", (long) Float.BYTES, finalFilePointer);
    }
}