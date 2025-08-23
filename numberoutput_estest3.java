package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * Unit tests for the {@link NumberOutput} class, focusing on edge cases and invalid inputs.
 */
public class NumberOutputTest {

    /**
     * Verifies that calling {@code outputLong} with a null buffer
     * throws a {@link NullPointerException}. The method is optimized for performance
     * and relies on the caller to provide a valid buffer.
     */
    @Test(expected = NullPointerException.class)
    public void outputLong_withNullBuffer_shouldThrowNullPointerException() {
        // The method is expected to fail fast when given a null buffer.
        // The actual value and offset are irrelevant for this test, so we use simple placeholders.
        long anyValue = 123L;
        int anyOffset = 0;
        char[] nullBuffer = null;

        NumberOutput.outputLong(anyValue, nullBuffer, anyOffset);
    }
}