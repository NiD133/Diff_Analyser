package com.fasterxml.jackson.core.io;

import org.junit.Test;
import java.nio.charset.StandardCharsets;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link NumberOutput} class, focusing on writing long values to byte arrays.
 */
public class NumberOutputTest {

    /**
     * Tests that {@link NumberOutput#outputLong(long, byte[], int)} correctly writes a positive,
     * 10-digit long value into a byte array.
     * It verifies both the number of bytes written and the actual string content.
     */
    @Test
    public void outputLongShouldCorrectlyWritePositiveTenDigitNumber() {
        // Arrange
        final long numberToWrite = 2084322364L;
        final String expectedString = "2084322364";
        final int expectedBytesWritten = expectedString.length();
        final byte[] buffer = new byte[30]; // A buffer with ample space.
        final int initialOffset = 0;

        // Act
        int actualBytesWritten = NumberOutput.outputLong(numberToWrite, buffer, initialOffset);

        // Assert
        // 1. Verify that the method returns the correct number of bytes written.
        assertEquals("The number of bytes written should match the number of digits.",
                expectedBytesWritten, actualBytesWritten);

        // 2. Verify that the content written to the buffer is the correct ASCII representation of the number.
        String actualString = new String(buffer, initialOffset, actualBytesWritten, StandardCharsets.US_ASCII);
        assertEquals("The string representation in the buffer should match the expected number.",
                expectedString, actualString);
    }
}