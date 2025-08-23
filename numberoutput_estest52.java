package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * Unit tests for the {@link NumberOutput} class, focusing on edge cases and exception handling.
 */
// The original class name 'NumberOutput_ESTestTest52' is kept to match the request,
// but in a real-world scenario, it would be renamed to something like 'NumberOutputTest'.
public class NumberOutput_ESTestTest52 extends NumberOutput_ESTest_scaffolding {

    /**
     * Verifies that calling {@code outputLong} with a null character buffer
     * throws a {@code NullPointerException}. This is the expected behavior,
     * as the method requires a valid buffer to write into.
     */
    @Test(expected = NullPointerException.class)
    public void outputLongShouldThrowNullPointerExceptionForNullBuffer() {
        // Arrange: Define inputs, where the buffer is intentionally null.
        long anyValue = 12345L;
        int anyOffset = 0;
        char[] nullBuffer = null;

        // Act: Call the method under test with the null buffer.
        // Assert: The @Test(expected) annotation handles the exception assertion.
        NumberOutput.outputLong(anyValue, nullBuffer, anyOffset);
    }
}