package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Contains unit tests for the {@link NumberOutput} class.
 */
public class NumberOutputTest {

    /**
     * Tests that `outputLong` correctly writes a single-digit long value
     * into a byte array at a specified offset.
     */
    @Test
    public void outputLong_shouldWriteSingleDigitValueToByteArray() {
        // Arrange: Set up the input value, buffer, and expected results.
        long valueToWrite = 1L;
        byte[] buffer = new byte[2];
        int initialOffset = 0;

        // The method should write one character ('1'), so the new offset will be 1.
        int expectedLength = 1;
        int expectedNewOffset = initialOffset + expectedLength;

        // The expected buffer after writing '1' at index 0. The second byte remains unchanged.
        byte[] expectedBuffer = new byte[]{'1', (byte) 0};

        // Act: Call the method under test.
        int actualNewOffset = NumberOutput.outputLong(valueToWrite, buffer, initialOffset);

        // Assert: Verify that the returned offset is correct and the buffer was modified as expected.
        assertEquals("The new offset should be the initial offset plus the number of characters written.",
                expectedNewOffset, actualNewOffset);
        assertArrayEquals("The buffer should contain the string representation of the long value.",
                expectedBuffer, buffer);
    }
}