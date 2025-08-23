package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * Unit tests for the {@link NumberOutput} class, focusing on edge cases and invalid inputs.
 */
public class NumberOutputTest {

    /**
     * Verifies that calling {@code outputLong} with a negative offset
     * correctly throws an {@link ArrayIndexOutOfBoundsException}.
     * The method must validate the offset parameter before attempting to write to the buffer.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void outputLong_shouldThrowException_whenOffsetIsNegative() {
        // Arrange: Define the conditions for the test.
        // A buffer of any reasonable size is sufficient.
        char[] buffer = new char[20];
        // The specific long value is irrelevant for this boundary check.
        long anyValue = 123L;
        // Use the simplest invalid case for a negative offset.
        int negativeOffset = -1;

        // Act: Call the method under test with invalid input.
        // The @Test(expected=...) annotation will automatically handle the assertion.
        NumberOutput.outputLong(anyValue, buffer, negativeOffset);
    }
}