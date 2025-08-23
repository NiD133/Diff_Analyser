package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * Unit tests for the {@link NumberOutput} class, focusing on handling invalid arguments.
 */
public class NumberOutputTest {

    /**
     * Verifies that calling {@code outputInt} with a null character buffer
     * correctly throws a {@link NullPointerException}. This ensures robust
     * handling of invalid input.
     */
    @Test(expected = NullPointerException.class)
    public void outputIntShouldThrowNullPointerExceptionForNullBuffer() {
        // Arrange: Define an integer value and offset, but use a null buffer.
        int value = 12;
        int offset = 0;
        char[] nullBuffer = null;

        // Act & Assert: Calling outputInt with a null buffer should throw a NullPointerException.
        NumberOutput.outputInt(value, nullBuffer, offset);
    }
}