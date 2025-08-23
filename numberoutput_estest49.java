package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test suite contains tests for the {@link NumberOutput} class.
 * This specific test case was refactored from an auto-generated test to improve clarity.
 */
public class NumberOutputTest {

    /**
     * Verifies that calling {@code outputLong} with an offset that is outside
     * the bounds of the destination buffer correctly throws an
     * {@link ArrayIndexOutOfBoundsException}.
     */
    @Test
    public void outputLong_shouldThrowException_whenOffsetIsOutOfBounds() {
        // Arrange: Create an empty buffer and an offset that is clearly invalid.
        byte[] emptyBuffer = new byte[0];
        int invalidOffset = 1436;
        long anyLongValue = 99999999999988L; // The specific value is not important for this test.

        // Act & Assert: Attempt the operation and verify the expected exception is thrown.
        try {
            NumberOutput.outputLong(anyLongValue, emptyBuffer, invalidOffset);
            // If this line is reached, the test has failed because no exception was thrown.
            fail("Expected an ArrayIndexOutOfBoundsException to be thrown.");
        } catch (ArrayIndexOutOfBoundsException e) {
            // The exception was caught as expected, so the test passes.
            // For a more robust test, we can optionally verify the exception message.
            // The standard message for this exception often includes the invalid index.
            String expectedMessageContent = String.valueOf(invalidOffset);
            assertTrue(
                "The exception message should contain the invalid offset: " + expectedMessageContent,
                e.getMessage().contains(expectedMessageContent)
            );
        }
    }
}