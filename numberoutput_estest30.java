package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

/**
 * Unit tests for the {@link NumberOutput} class, focusing on writing long values.
 */
public class NumberOutputTest {

    /**
     * Tests that `outputLong` correctly writes a positive long value into a char array
     * at a specified offset and returns the correct new offset.
     */
    @Test
    public void outputLong_shouldWritePositiveLongToCharArrayAndReturnNewOffset() {
        // Arrange
        long valueToWrite = 2084322237L;
        String valueAsString = "2084322237";
        int initialOffset = 4;
        
        // Create a buffer large enough to hold the number after the offset.
        // The buffer is intentionally larger to ensure the method doesn't write out of bounds.
        char[] buffer = new char[20]; 
        
        // The expected offset is the initial offset plus the length of the number's string representation.
        int expectedNewOffset = initialOffset + valueAsString.length();
        
        // Construct the expected final state of the buffer for a complete assertion.
        // The first 4 chars should be untouched (null chars), followed by the number.
        char[] expectedBuffer = new char[20];
        System.arraycopy(valueAsString.toCharArray(), 0, expectedBuffer, initialOffset, valueAsString.length());

        // Act
        int actualNewOffset = NumberOutput.outputLong(valueToWrite, buffer, initialOffset);

        // Assert
        // 1. Verify that the returned offset is correct.
        assertEquals("The new offset should be the initial offset plus the number's length.",
                expectedNewOffset, actualNewOffset);

        // 2. Verify that the buffer contains the correct character representation of the number.
        // This is a more robust check than just verifying the substring.
        assertArrayEquals("The buffer should contain the correct string representation of the long.",
                expectedBuffer, buffer);
    }
}