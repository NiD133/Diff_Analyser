package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link NumberOutput} class, focusing on integer to byte array conversion.
 */
public class NumberOutputTest {

    /**
     * Tests that a positive four-digit integer is correctly written as ASCII bytes
     * into a byte array at a specified offset.
     */
    @Test
    public void outputIntShouldWritePositiveIntegerToByteArrayAtGivenOffset() {
        // Arrange
        final int numberToWrite = 1497;
        final int initialOffset = 2;
        
        // The buffer needs space for the offset and the 4 digits of the number.
        // offset (2) + number length (4) = 6
        byte[] buffer = new byte[6];

        // The expected buffer state after the operation.
        // The first two bytes are untouched (default 0).
        // The number "1497" is written as ASCII bytes starting from index 2.
        byte[] expectedBuffer = new byte[]{
            (byte) 0, (byte) 0, (byte) '1', (byte) '4', (byte) '9', (byte) '7'
        };
        
        // The expected final offset is the initial offset plus the number of digits written.
        // 2 (initial) + 4 (digits) = 6
        final int expectedFinalOffset = 6;

        // Act
        int finalOffset = NumberOutput.outputInt(numberToWrite, buffer, initialOffset);

        // Assert
        assertEquals("The final offset should be the initial offset plus the number of digits.",
                expectedFinalOffset, finalOffset);
        assertArrayEquals("The buffer should contain the number written at the correct offset.",
                expectedBuffer, buffer);
    }
}