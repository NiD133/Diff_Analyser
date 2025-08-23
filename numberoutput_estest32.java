package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link NumberOutput} class, focusing on integer serialization.
 */
public class NumberOutputTest {

    /**
     * Verifies that outputInt correctly writes the character '0' to a buffer
     * when given the integer value 0.
     */
    @Test
    public void outputInt_whenWritingZero_shouldPlaceCharZeroInBuffer() {
        // Arrange: Set up the test conditions and expected results.
        int valueToWrite = 0;
        int initialOffset = 0;
        char[] buffer = new char[10]; // A buffer with sufficient space.

        // The expected state of the buffer after writing '0'.
        char[] expectedBuffer = {'0', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000'};
        // The expected new offset should be 1, as one character was written.
        int expectedNewOffset = 1;

        // Act: Call the method under test.
        int actualNewOffset = NumberOutput.outputInt(valueToWrite, buffer, initialOffset);

        // Assert: Verify the results.
        assertEquals("The new offset should be advanced by one character.", expectedNewOffset, actualNewOffset);
        assertArrayEquals("The buffer should contain the character '0' at the start.", expectedBuffer, buffer);
    }
}