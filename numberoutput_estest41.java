package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link NumberOutput} class.
 */
public class NumberOutputTest {

    /**
     * Verifies that calling {@code NumberOutput.outputInt} with the value 0
     * correctly writes the character '0' into the provided byte array.
     * It also checks that the method returns the correct new offset within the buffer.
     */
    @Test
    public void outputInt_withZero_writesCharZeroAndReturnsCorrectOffset() {
        // Arrange: Set up the input value and the output buffer.
        int valueToOutput = 0;
        byte[] buffer = new byte[5];
        int initialOffset = 0;

        // Define the expected state of the buffer after the call.
        byte[] expectedBuffer = new byte[]{(byte) '0', 0, 0, 0, 0};
        // The method should write one character ('0'), so the new offset is initialOffset + 1.
        int expectedNewOffset = 1;

        // Act: Call the method under test.
        int actualNewOffset = NumberOutput.outputInt(valueToOutput, buffer, initialOffset);

        // Assert: Verify that the buffer was modified as expected and the returned offset is correct.
        assertArrayEquals("The buffer should contain the character '0' at the beginning.",
                expectedBuffer, buffer);
        assertEquals("The returned offset should be 1 after writing a single-digit number.",
                expectedNewOffset, actualNewOffset);
    }
}