package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Tests that calling read() with a negative length returns -1 (indicating end-of-stream)
     * and does not consume a previously pushed-back byte.
     *
     * This verifies the class's specific handling of invalid arguments, where a negative
     * length parameter takes precedence over other invalid parameters like an out-of-bounds offset.
     */
    @Test
    public void readWithNegativeLength_shouldReturnNegativeOne_andNotConsumePushedBackByte() throws IOException {
        // Arrange
        byte[] sourceData = new byte[8];
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(sourceData);
        assertEquals("Initial file pointer should be at the beginning", 0L, fileOrArray.getFilePointer());

        // Push a byte back into the stream. The effective file pointer should now be -1.
        byte byteToPushBack = (byte) 0xFF;
        fileOrArray.pushBack(byteToPushBack);
        assertEquals("File pointer should be at -1 after pushBack", -1L, fileOrArray.getFilePointer());

        byte[] destinationBuffer = new byte[8];
        int outOfBoundsOffset = 10;
        int negativeLength = -50;

        // Act
        // Attempt to read with a negative length and an out-of-bounds offset.
        int bytesRead = fileOrArray.read(destinationBuffer, outOfBoundsOffset, negativeLength);

        // Assert
        // The method should return -1 because the length is negative, which is treated as an end-of-stream condition.
        assertEquals("read() with a negative length should return -1", -1, bytesRead);

        // The state of the reader should not have changed; the pushed-back byte must still be available.
        assertEquals("File pointer should remain at -1", -1L, fileOrArray.getFilePointer());

        // Further verification: the next read() should successfully retrieve the pushed-back byte.
        int nextByteRead = fileOrArray.read();
        assertEquals("The pushed-back byte should be readable on the next call", byteToPushBack & 0xFF, nextByteRead);
        assertEquals("File pointer should advance to 0 after reading the pushed-back byte", 0L, fileOrArray.getFilePointer());
    }
}