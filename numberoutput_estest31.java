package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link NumberOutput} class, focusing on long-to-char conversion.
 */
public class NumberOutputTest {

    /**
     * Tests that {@link NumberOutput#outputLong(long, char[], int)} correctly converts
     * a negative long value into its character representation in a buffer.
     */
    @Test
    public void shouldWriteNegativeLongToCharArray() {
        // Arrange: Set up the input value and the output buffer.
        long valueToTest = -3640L;
        String expectedString = "-3640";
        // The buffer must be large enough to hold the string representation.
        char[] buffer = new char[10];
        int initialOffset = 0;

        // Act: Call the method under test.
        int newOffset = NumberOutput.outputLong(valueToTest, buffer, initialOffset);

        // Assert: Verify the method's output and return value.
        // 1. The method should return the new offset, which is the number of characters written.
        int charsWritten = expectedString.length();
        assertEquals("The method should return the new offset after writing the number.",
                charsWritten, newOffset);

        // 2. The buffer should contain the correct string representation of the number.
        String actualString = new String(buffer, initialOffset, charsWritten);
        assertEquals("The buffer should contain the correct string representation of the negative long.",
                expectedString, actualString);
    }
}