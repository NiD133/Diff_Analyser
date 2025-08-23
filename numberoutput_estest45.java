package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * Unit tests for the {@link NumberOutput} class, focusing on edge cases and error handling.
 */
public class NumberOutputTest {

    /**
     * Verifies that calling {@link NumberOutput#outputInt(int, char[], int)} with a null
     * character buffer correctly throws a {@link NullPointerException}.
     */
    @Test(expected = NullPointerException.class)
    public void outputIntShouldThrowNullPointerExceptionWhenBufferIsNull() {
        // Arrange: Define arbitrary input values, with a null buffer.
        int anyNumber = 123;
        int anyOffset = 0;
        char[] nullBuffer = null;

        // Act & Assert: Calling outputInt with a null buffer should throw a NullPointerException.
        NumberOutput.outputInt(anyNumber, nullBuffer, anyOffset);
    }
}