package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * Unit tests for the {@link NumberOutput} class, focusing on edge cases and invalid inputs.
 */
public class NumberOutputTest {

    /**
     * Verifies that {@code outputLong} throws an {@link ArrayIndexOutOfBoundsException}
     * when provided with a negative offset. A negative offset is an invalid
     * starting position in the output buffer.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void outputLongShouldThrowExceptionForNegativeOffset() {
        // Arrange: Set up the arguments for the method call.
        // The specific long value is not important for this test.
        long value = 12345L;
        // The buffer must be large enough to not cause an error on its own,
        // isolating the negative offset as the reason for the exception.
        char[] buffer = new char[20];
        int negativeOffset = -1;

        // Act: Call the method with the invalid negative offset.
        // The @Test(expected=...) annotation will assert that the correct exception is thrown.
        NumberOutput.outputLong(value, buffer, negativeOffset);
    }
}